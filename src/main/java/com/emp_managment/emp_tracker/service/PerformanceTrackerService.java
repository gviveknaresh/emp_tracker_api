package com.emp_managment.emp_tracker.service;

import com.emp_managment.emp_tracker.dto.CycleSummaryResponseDTO;
import com.emp_managment.emp_tracker.dto.EmployeeReviewResponseDTO;
import com.emp_managment.emp_tracker.dto.PerformanceReviewDTO;
import org.springframework.data.domain.Page;

public interface PerformanceTrackerService {

    void saveReviews(PerformanceReviewDTO performanceReviewDTO);

    Page<EmployeeReviewResponseDTO> fetchReviewsByEmployeeId(Long employeeId, Integer page, Integer size);

    CycleSummaryResponseDTO getCycleSummary(Long cycleId);
}
