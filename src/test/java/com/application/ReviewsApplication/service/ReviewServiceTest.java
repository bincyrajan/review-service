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
        review.setAuthor("John");
        review.setReviewSource("Amazon");
        review.setReviewedDate(LocalDateTime.now());

        Mockito.when(repository.save(Mockito.any(Review.class))).thenReturn(review);
        Assertions.assertEquals(review, service.saveReview(review));
    }

    @Test
    void testGetReviewsOnlyDate() {
        LocalDate date = LocalDate.of(2024, 6, 25);
        List<Review> mockList = List.of(new Review());

        Mockito.when(repository.findByReviewedDateBetween(Mockito.any(), Mockito.any())).thenReturn(mockList);

        List<Review> reviews = service.getReviews(Optional.of(date), Optional.empty(), Optional.empty());
        Assertions.assertEquals(1, reviews.size());
    }

    @Test
    void testGetMonthlyAverages() {
        Object[] row = new Object[]{"Amazon", "2024-06", 4.0};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(row);

        Mockito.when(repository.getMonthlyAverageRatings()).thenReturn(mockResult);

        Map<String, Map<String, Double>> result = service.getMonthlyAverages();
        Assertions.assertEquals(4.0, result.get("Amazon").get("2024-06"));
    }

    @Test
    void testGetRatingCounts() {
        Object[] row = new Object[]{5, 1L};
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(row);

        Mockito.when(repository.getRatingCountsFromDB()).thenReturn(mockResult);

        Map<Integer, Long> result = service.getRatingCounts();
        Assertions.assertEquals(1L, result.get(5));
    }
}
