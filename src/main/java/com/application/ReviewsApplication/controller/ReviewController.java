package com.application.ReviewsApplication.controller;

import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<Review> saveReview(@RequestBody @Valid Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Integer rating) {
        return ResponseEntity.ok(service.getReviews(Optional.ofNullable(date), Optional.ofNullable(source), Optional.ofNullable(rating)));
    }

    @GetMapping("/average-monthly")
    public ResponseEntity<Map<String, Map<String, Double>>> getMonthlyAverages() {
        return ResponseEntity.ok(service.getMonthlyAverages());
    }

    @GetMapping("/rating-counts")
    public ResponseEntity<Map<Integer, Long>> getRatingCounts() {
        return ResponseEntity.ok(service.getRatingCounts());
    }
}