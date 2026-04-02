package com.emp_managment.emp_tracker.repository;

import com.emp_managment.emp_tracker.dto.EmployeeReviewResponseDTO;
import com.emp_managment.emp_tracker.entity.PerformanceReviewEntity;
import com.emp_managment.emp_tracker.projections.TopPerformerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReviewEntity, Long> {

    List<PerformanceReviewEntity> findByCycleId(Long cycleId);

    @Query("""
                SELECT AVG(pr.rating)
                FROM PerformanceReviewEntity pr
                WHERE pr.cycle.id = :cycleId
            """)
    Optional<Double> findAverageRatingByCycleId(@Param("cycleId") Long cycleId);

    @Query("""
                SELECT pr.employee.id   AS employeeId,
                       pr.employee.name AS employeeName,
                       pr.rating        AS rating
                FROM PerformanceReviewEntity pr
                WHERE pr.cycle.id = :cycleId
                  AND pr.rating >= :minRating
                ORDER BY pr.rating DESC
            """)
    List<TopPerformerProjection> findTopPerformersByCycleId(
            @Param("cycleId") Long cycleId,
            @Param("minRating") int minRating,
            Pageable pageable
    );

    @Query("""
                SELECT new com.emp_managment.emp_tracker.dto.EmployeeReviewResponseDTO(
                    pr.id, pr.reviewerId, pr.rating,
                    pr.reviewerNotes, pr.submittedAt,
                    c.id, c.name
                )
                FROM PerformanceReviewEntity pr
                LEFT JOIN pr.cycle c
                WHERE pr.employeeId = :employeeId
            """)
    Page<EmployeeReviewResponseDTO> fetchReviewsByEmployeeId(@Param("employeeId") Long employeeId, Pageable pageable);

}
