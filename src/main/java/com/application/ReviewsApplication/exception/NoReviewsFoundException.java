package com.application.ReviewsApplication.exception;

public class NoReviewsFoundException extends RuntimeException {
    public NoReviewsFoundException(String message) {
        super(message);
    }
}
