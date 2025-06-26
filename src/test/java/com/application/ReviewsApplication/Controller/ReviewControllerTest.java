package com.application.ReviewsApplication.Controller;
import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void testSaveReview() throws Exception {
        Review review = new Review();
        review.setId(1L);
        review.setReview("Good");
        review.setRating(4);
        review.setReviewSource("Amazon");
        review.setReviewedDate(LocalDateTime.now());

        Mockito.when(reviewService.saveReview(Mockito.any(Review.class)))
                .thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetReviewsWithoutParams() throws Exception {
        List<Review> reviews = List.of(new Review());
        Mockito.when(reviewService.getReviews(Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(reviews);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk());
    }


    @Test
    void testGetMonthlyAverages() throws Exception {
        Map<String, Map<String, Double>> mockData = Map.of("Amazon", Map.of("2024-06", 4.0));
        Mockito.when(reviewService.getMonthlyAverages()).thenReturn(mockData);

        mockMvc.perform(get("/api/reviews/average-monthly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Amazon.2024-06").value(4.0));
    }

    @Test
    void testGetRatingCounts() throws Exception {
        Map<Integer, Long> counts = Map.of(5, 2L);
        Mockito.when(reviewService.getRatingCounts()).thenReturn(counts);

        mockMvc.perform(get("/api/reviews/rating-counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.5").value(2));
    }

    @Test
    void testGetReviewsWithInvalidDate() throws Exception {
        mockMvc.perform(get("/api/reviews")
                        .param("date", "2024-31-12"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Invalid date format")));
    }


}
