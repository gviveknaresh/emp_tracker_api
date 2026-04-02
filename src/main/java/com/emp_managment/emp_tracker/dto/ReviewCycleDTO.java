package com.emp_managment.emp_tracker.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ReviewCycleDTO implements Serializable {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
