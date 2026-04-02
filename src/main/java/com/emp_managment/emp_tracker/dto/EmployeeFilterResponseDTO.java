package com.emp_managment.emp_tracker.dto;

import com.emp_managment.emp_tracker.projections.EmployeeRatingSummaryProjection;
import lombok.Value;

@Value
public class EmployeeFilterResponseDTO {

    Long   employeeId;
    String employeeName;
    String department;
    double avgRating;
    long   reviewCount;

    public static EmployeeFilterResponseDTO from(EmployeeRatingSummaryProjection p) {
        return new EmployeeFilterResponseDTO(
                p.getEmployeeId(),
                p.getEmployeeName(),
                p.getDepartment(),
                p.getAvgRating(),
                p.getReviewCount()
        );
    }
}
