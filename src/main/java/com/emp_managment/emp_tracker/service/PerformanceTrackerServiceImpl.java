package com.emp_managment.emp_tracker.service;

import com.emp_managment.emp_tracker.dto.CycleSummaryResponseDTO;
import com.emp_managment.emp_tracker.dto.EmployeeReviewResponseDTO;
import com.emp_managment.emp_tracker.dto.PerformanceReviewDTO;
import com.emp_managment.emp_tracker.entity.PerformanceReviewEntity;
import com.emp_managment.emp_tracker.enums.GoalStatus;
import com.emp_managment.emp_tracker.projections.GoalStatusCountProjection;
import com.emp_managment.emp_tracker.repository.GoalsRepository;
import com.emp_managment.emp_tracker.repository.PerformanceReviewRepository;
import com.emp_managment.emp_tracker.repository.ReviewCycleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceTrackerServiceImpl implements PerformanceTrackerService {

    private final PerformanceReviewRepository performanceReviewRepository;
    private final GoalsRepository goalsRepository;
    private final ReviewCycleRepository reviewCycleRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PerformanceTrackerServiceImpl(PerformanceReviewRepository performanceReviewRepository,
                                         GoalsRepository goalsRepository, ReviewCycleRepository reviewCycleRepository) {
        this.performanceReviewRepository = performanceReviewRepository;
        this.goalsRepository = goalsRepository;
        this.reviewCycleRepository = reviewCycleRepository;
    }

    /**
     * Currently using id only association pattern while saving foreign ids in PerformanceReviewEntity
     * it saves 3 records being hydrated and managed by hibernate while only creating a join
     * *** this is based on assumption that the id will be present in join table, if not it's better to use referenceById and save ***
     */

    @Override
    @Transactional
    public void saveReviews(PerformanceReviewDTO performanceReviewDTO) {
        PerformanceReviewEntity performanceReviewEntity = new PerformanceReviewEntity();
        performanceReviewEntity.setReviewerId(performanceReviewDTO.getReviewerId());
        performanceReviewEntity.setCycleId(performanceReviewDTO.getCycleId());
        performanceReviewEntity.setEmployeeId(performanceReviewDTO.getEmployeeId());
        performanceReviewEntity.setRating(performanceReviewDTO.getRating());
        performanceReviewEntity.setReviewerNotes(performanceReviewDTO.getReviewerNotes());
        performanceReviewEntity.setSubmittedAt(performanceReviewDTO.getSubmittedAt());
        performanceReviewRepository.save(performanceReviewEntity);
    }

    /**
     * DTO Projection and pageable query used to only fetch required fields and not load full dataset in memory respectively
     */
    @Transactional(readOnly = true)
    public Page<EmployeeReviewResponseDTO> fetchReviewsByEmployeeId(Long employeeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return performanceReviewRepository.fetchReviewsByEmployeeId(employeeId, pageable);
    }


    private static final int TOP_PERFORMER_MIN_RATING = 4;
    private static final int TOP_PERFORMER_LIMIT = 10;

    /**
     * This is based on assumption that review records are in an around 50k
     * If the scale exceeds this threshold, on fly calculation will be an overhead,
     * 1] it's better to maintain separate table and update the avg through an Async method or Transactional Outbox pattern
     * 2] Smaller, focused queries were used instead of a single complex query so that they could be optimized or cached separately.
     * also in case that, it would be used someplace else.
     * but on a higher scale it will be better to cache service method CycleSummaryResponseDTO response
     * and updating at regular intervals.
     * <p>
     * Assumptions of business requirement,
     * TOP_PERFORMER_LIMIT
     * TOP_PERFORMER_MIN_RATING
     */

    @Override
    @Transactional(readOnly = true)
    public CycleSummaryResponseDTO getCycleSummary(Long cycleId) {

        if (!reviewCycleRepository.existsById(cycleId)) {
            throw new EntityNotFoundException("Cycle not found: " + cycleId);
        }

        double avgRating = performanceReviewRepository
                .findAverageRatingByCycleId(cycleId)
                .orElse(0.0);

        // 3. Top performers — rating >= 4, capped at 10, ordered DESC
        Pageable page = PageRequest.of(0, TOP_PERFORMER_LIMIT);
        List<CycleSummaryResponseDTO.TopPerformerDto> topPerformers = performanceReviewRepository
                .findTopPerformersByCycleId(cycleId, TOP_PERFORMER_MIN_RATING, page)
                .stream()
                .map(p -> new CycleSummaryResponseDTO.TopPerformerDto(
                        p.getEmployeeId(),
                        p.getEmployeeName(),
                        p.getRating()
                ))
                .toList();

        Map<String, Long> statusCounts = goalsRepository
                .countGoalStatusByCycleId(cycleId)
                .stream()
                .collect(Collectors.toMap(
                        GoalStatusCountProjection::getStatus,
                        GoalStatusCountProjection::getCount
                ));

        long completed = statusCounts.getOrDefault(GoalStatus.COMPLETED.toString(), 0L);
        long incompleted = statusCounts.getOrDefault(GoalStatus.MISSED.toString(), 0L);

        return new CycleSummaryResponseDTO(
                cycleId,
                avgRating,
                topPerformers,
                completed,
                incompleted
        );
    }
}
