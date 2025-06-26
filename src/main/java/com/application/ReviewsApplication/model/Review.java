package com.application.ReviewsApplication.model;
import java.time.LocalDateTime;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String review;
    private String author;
    private String reviewSource;
    @Min(1)
    @Max(5)
    private int rating;
    private String title;
    private String productName;
    private LocalDateTime reviewedDate;
}