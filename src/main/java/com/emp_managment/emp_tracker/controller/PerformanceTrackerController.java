package com.emp_managment.emp_tracker.controller;

import com.emp_managment.emp_tracker.dto.CycleSummaryResponseDTO;
import com.emp_managment.emp_tracker.dto.EmployeeReviewResponseDTO;
import com.emp_managment.emp_tracker.dto.PerformanceReviewDTO;
import com.emp_managment.emp_tracker.service.PerformanceTrackerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance-reviews")
@Slf4j
public class PerformanceTrackerController {

    private final PerformanceTrackerService performanceTrackerService;

    @Autowired
    public PerformanceTrackerController(PerformanceTrackerService performanceTrackerService) {
        this.performanceTrackerService = performanceTrackerService;
    }

    /**
     * Save Performance Review
     * @param reviewDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<?> savePerformanceReview(@Valid @RequestBody PerformanceReviewDTO reviewDTO) {
        performanceTrackerService.saveReviews(reviewDTO);
        log.info("Performance review saved for employeeId: {} by reviewerId: {}",
                reviewDTO.getEmployeeId(), reviewDTO.getReviewerId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Performance review saved successfully");
    }

    /**
     * Get all reviews for a specific employee including cycle details
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EmployeeReviewResponseDTO>> getReviewsByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<EmployeeReviewResponseDTO> reviews =
                performanceTrackerService.fetchReviewsByEmployeeId(employeeId, page, size);
        log.info("Returning Employee Review By for employeeId: {}", employeeId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get Summary Counts based on cycle
     */
    @GetMapping("/cycles/{id}/summary")
    public ResponseEntity<?> getCycleSummary(@PathVariable Long cycleId) {
        CycleSummaryResponseDTO responseDTOS = performanceTrackerService.getCycleSummary(cycleId);
        log.info("Returning Cycle Summary for cycleId: {}", cycleId);
        return ResponseEntity.ok(responseDTOS);
    }
}
