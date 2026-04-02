package com.emp_managment.emp_tracker.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public record EmployeeFilterRequest(

        @RequestParam(required = true)
        String department,

        @RequestParam(defaultValue = "0.0")
        @DecimalMin(value = "1.0", message = "minRating must be between 1 and 5")
        @DecimalMax(value = "5.0", message = "minRating must be between 1 and 5")
        double minRating,

        @RequestParam(defaultValue = "0")
        @Min(0) int page,

        @RequestParam(defaultValue = "20")
        @Min(1) @Max(100) int size
) {}