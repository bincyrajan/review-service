package com.application.ReviewsApplication.service;

import com.application.ReviewsApplication.exception.NoReviewsFoundException;
import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public Review saveReview(Review review) {
        return repository.save(review);
    }

    public List<Review> getReviews(Optional<LocalDate> date, Optional<String> source, Optional<Integer> rating) {
        List<Review> reviews;

        if (date.isPresent()) {
            LocalDateTime start = date.get().atStartOfDay();
            LocalDateTime end = date.get().atTime(LocalTime.MAX);

            if (source.isPresent() && rating.isPresent()) {
                reviews = repository.findByReviewedDateBetweenAndReviewSourceAndRating(start, end, source.get(), rating.get());
            } else if (source.isPresent()) {
                reviews = repository.findByReviewedDateBetweenAndReviewSource(start, end, source.get());
            } else if (rating.isPresent()) {
                reviews = repository.findByReviewedDateBetweenAndRating(start, end, rating.get());
            } else {
                reviews = repository.findByReviewedDateBetween(start, end);
            }
        } else if (source.isPresent() && rating.isPresent()) {
            reviews = repository.findByReviewSourceAndRating(source.get(), rating.get());
        } else if (source.isPresent()) {
            reviews = repository.findByReviewSource(source.get());
        } else if (rating.isPresent()) {
            reviews = repository.findByRating(rating.get());
        } else {
            reviews = repository.findAll();
        }

        if (reviews.isEmpty()) {
            throw new NoReviewsFoundException("No reviews found matching the criteria.");
        }

        return reviews;
    }


    public Map<String, Map<String, Double>> getMonthlyAverages() {
        List<Review> allReviews = repository.findAll();
        if (allReviews.isEmpty()) {
            throw new NoReviewsFoundException("No reviews found for monthly averages.");
        }

        Map<String, Map<String, List<Integer>>> temp = new TreeMap<>();

        for (Review r : allReviews) {
            String source = r.getReviewSource();
            String monthKey = r.getReviewedDate().getYear() + "-" + String.format("%02d", r.getReviewedDate().getMonthValue());

            temp.computeIfAbsent(source, k -> new TreeMap<>())
                    .computeIfAbsent(monthKey, k -> new ArrayList<>())
                    .add(r.getRating());
        }

        Map<String, Map<String, Double>> result = new TreeMap<>();
        for (var entry : temp.entrySet()) {
            Map<String, Double> avgMap = new TreeMap<>();
            for (var inner : entry.getValue().entrySet()) {
                List<Integer> ratings = inner.getValue();
                double avg = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                avgMap.put(inner.getKey(), avg);
            }
            result.put(entry.getKey(), avgMap);
        }

        return result;
    }


    public Map<Integer, Long> getRatingCounts() {
        List<Review> allReviews = repository.findAll();
        if (allReviews.isEmpty()) {
            throw new NoReviewsFoundException("No rating counts found.");
        }

        return allReviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getRating,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }


}
