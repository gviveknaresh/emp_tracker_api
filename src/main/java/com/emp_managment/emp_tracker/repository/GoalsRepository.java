package com.emp_managment.emp_tracker.repository;

import com.emp_managment.emp_tracker.entity.GoalsEntity;
import com.emp_managment.emp_tracker.projections.GoalStatusCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<GoalsEntity, Long> {

    @Query("""
                SELECT g.status AS status, COUNT(g) AS count
                FROM GoalsEntity g
                WHERE g.cycle.id = :cycleId
                GROUP BY g.status
            """)
    List<GoalStatusCountProjection> countGoalStatusByCycleId(@Param("cycleId") Long cycleId);
}