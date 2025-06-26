package com.application.ReviewsApplication.controller;

import com.application.ReviewsApplication.model.Review;
import com.application.ReviewsApplication.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<Review> saveReview(@RequestBody @Valid Review review) {
        return ResponseEntity.ok(service.saveReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Optional<String> source,
            @RequestParam Optional<Integer> rating) {
        return ResponseEntity.ok(service.getReviews(Optional.ofNullable(date), source, rating));
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