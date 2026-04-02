package com.emp_managment.emp_tracker.repository;

import com.emp_managment.emp_tracker.entity.EmployeeEntity;
import com.emp_managment.emp_tracker.projections.EmployeeRatingSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    boolean existsByEmployeeCode(String employeeCode);


    @Query("""
                SELECT e.id         AS employeeId,
                       e.name       AS employeeName,
                       e.department AS department,
                       AVG(pr.rating)  AS avgRating,
                       COUNT(pr.id)    AS reviewCount
                FROM EmployeeEntity e
                LEFT JOIN e.reviews pr
                WHERE e.department = :department
                  AND pr.rating IS NOT NULL
                GROUP BY e.id, e.name, e.department
                HAVING AVG(pr.rating) >= :minRating
                ORDER BY avgRating DESC
            """)
    Page<EmployeeRatingSummaryProjection> findByDepartmentAndMinRating(
            @Param("department") String department,
            @Param("minRating") double minRating,
            Pageable pageable
    );
}
