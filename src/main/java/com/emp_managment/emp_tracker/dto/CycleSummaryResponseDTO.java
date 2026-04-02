package com.emp_managment.emp_tracker.dto;

import lombok.Value;

import java.util.List;

@Value
public class CycleSummaryResponseDTO {
    Long   cycleId;
    Double averageRating;
    List<TopPerformerDto> topPerformers;
    long   completedGoals;
    long   incompletedGoals;

    @Value
    public static class TopPerformerDto {
        Long   employeeId;
        String employeeName;
        int    rating;
    }
}