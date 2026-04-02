package com.emp_managment.emp_tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PerformanceReviewDTO implements Serializable {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Cycle ID is required")
    private Long cycleId;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    @Size(max = 2000, message = "Reviewer notes cannot exceed 2000 characters")
    private String reviewerNotes;

    @NotNull(message = "Submission date is required")
    private LocalDateTime submittedAt;
}