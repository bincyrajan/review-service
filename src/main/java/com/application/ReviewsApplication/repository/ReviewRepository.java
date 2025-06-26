package com.application.ReviewsApplication.repository;


import com.application.ReviewsApplication.model.Review;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewedDateBetween(LocalDateTime start, LocalDateTime end);

    List<Review> findByReviewedDateBetweenAndReviewSource(LocalDateTime start, LocalDateTime end, String source);

    List<Review> findByReviewedDateBetweenAndRating(LocalDateTime start, LocalDateTime end, int rating);

    List<Review> findByReviewedDateBetweenAndReviewSourceAndRating(LocalDateTime start, LocalDateTime end, String source, int rating);

    List<Review> findByReviewSource(String source);

    List<Review> findByRating(int rating);

    List<Review> findByReviewSourceAndRating(String source, int rating);

}
