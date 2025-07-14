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

    @Query(value = "SELECT review_source AS source, " +
            "TO_CHAR(reviewed_date, 'YYYY-MM') AS month, " +
            "AVG(rating) AS avg_rating " +
            "FROM review " +
            "GROUP BY review_source, TO_CHAR(reviewed_date, 'YYYY-MM') " +
            "ORDER BY review_source, month", nativeQuery = true)
    List<Object[]> getMonthlyAverageRatings();

    @Query(value = "SELECT rating, COUNT(*) AS count " +
            "FROM review " +
            "GROUP BY rating " +
            "ORDER BY rating", nativeQuery = true)
    List<Object[]> getRatingCountsFromDB();
}


