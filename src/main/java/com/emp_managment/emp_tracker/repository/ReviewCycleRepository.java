package com.emp_managment.emp_tracker.repository;

import com.emp_managment.emp_tracker.entity.ReviewCycleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCycleRepository extends JpaRepository<ReviewCycleEntity, Long> {

    boolean existsById(String cycleId);

}
