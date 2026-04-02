package com.emp_managment.emp_tracker.projections;

public interface EmployeeRatingSummaryProjection {
    Long   getEmployeeId();
    String getEmployeeName();
    String getDepartment();
    Double getAvgRating();
    Long   getReviewCount();
}