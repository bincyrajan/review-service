package com.application.ReviewsApplication.model;
import java.time.LocalDateTime;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "review")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String review;

    @NotBlank
    private String author;

    @NotBlank
    private String reviewSource;

    @Min(1)
    @Max(5)
    private int rating;

    private String title;
    private String productName;

    @NotNull
    @PastOrPresent
    private LocalDateTime reviewedDate;
}
