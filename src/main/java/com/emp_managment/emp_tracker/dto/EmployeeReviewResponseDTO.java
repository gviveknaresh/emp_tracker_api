package com.emp_managment.emp_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmployeeReviewResponseDTO implements Serializable {
    private Long reviewId;
    private Long reviewerId;
    private Integer rating;
    private String reviewerNotes;
    private LocalDateTime submittedAt;
    private Long cycleId;
    private String cycleName;
}