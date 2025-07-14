package com.application.ReviewsApplication.service;

import com.application.ReviewsApplication.exception.NoReviewsFoundException;
import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository repository;

    public Review saveReview(Review review) {
        return repository.save(review);
    }

    public List<Review> getReviews(Optional<LocalDate> date, Optional<String> source, Optional<Integer> rating) {
        logger.debug("getReviews called with date={}, source={}, rating={}", date, source, rating);

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
            logger.warn("No reviews found for given criteria.");
            throw new NoReviewsFoundException("No reviews found matching the criteria.");
        }

        return reviews;
    }

    public Map<String, Map<String, Double>> getMonthlyAverages() {
        List<Object[]> results = repository.getMonthlyAverageRatings();

        if (results.isEmpty()) {
            logger.warn("No monthly average ratings found.");
            throw new NoReviewsFoundException("No reviews found for monthly averages.");
        }

        Map<String, Map<String, Double>> finalResult = new TreeMap<>();
        for (Object[] row : results) {
            String source = (String) row[0];
            String month = (String) row[1];
            Double avgRating = ((Number) row[2]).doubleValue();

            finalResult.computeIfAbsent(source, k -> new TreeMap<>()).put(month, avgRating);
        }

        return finalResult;
    }

    public Map<Integer, Long> getRatingCounts() {
        List<Object[]> rows = repository.getRatingCountsFromDB();

        if (rows.isEmpty()) {
            logger.warn("No rating counts found.");
            throw new NoReviewsFoundException("No rating counts found.");
        }

        Map<Integer, Long> result = new TreeMap<>();
        for (Object[] row : rows) {
            Integer rating = (Integer) row[0];
            Long count = ((Number) row[1]).longValue();
            result.put(rating, count);
        }

        return result;
    }
}
