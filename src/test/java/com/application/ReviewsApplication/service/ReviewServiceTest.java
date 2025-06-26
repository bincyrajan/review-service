package com.application.ReviewsApplication.service;

import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    private ReviewService service;

    @MockBean
    private ReviewRepository repository;

    @Test
    void testSaveReview() {
        Review review = new Review();
        review.setReview("Good");
        Mockito.when(repository.save(review)).thenReturn(review);
        Assertions.assertEquals(review, service.saveReview(review));
    }

    @Test
    void testGetReviewsOnlyDate() {
        LocalDate date = LocalDate.of(2024, 6, 25);
        List<Review> mockList = List.of(new Review());

        Mockito.when(repository.findByReviewedDateBetween(Mockito.any(), Mockito.any()))
                .thenReturn(mockList);

        List<Review> reviews = service.getReviews(Optional.of(date), Optional.empty(), Optional.empty());
        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    void testGetMonthlyAverages() {
        Review r1 = new Review();
        r1.setReviewSource("Amazon");
        r1.setReviewedDate(LocalDateTime.of(2024, 6, 15, 10, 0));
        r1.setRating(4);

        Mockito.when(repository.findAll()).thenReturn(List.of(r1));

        Map<String, Map<String, Double>> result = service.getMonthlyAverages();
        Assertions.assertEquals(4.0, result.get("Amazon").get("2024-06"));
    }

    @Test
    void testGetRatingCounts() {
        Review r1 = new Review();
        r1.setRating(5);

        Mockito.when(repository.findAll()).thenReturn(List.of(r1));

        Map<Integer, Long> result = service.getRatingCounts();
        Assertions.assertEquals(1, result.get(5));
    }
}
