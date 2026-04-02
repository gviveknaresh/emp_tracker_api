package com.emp_managment.emp_tracker.dto;

import com.emp_managment.emp_tracker.entity.EmployeeEntity;
import com.emp_managment.emp_tracker.entity.ReviewCycleEntity;
import com.emp_managment.emp_tracker.enums.GoalStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoalsDTO implements Serializable {
    private Long id;
    private EmployeeEntity employee;
    private ReviewCycleEntity cycle;
    private String title;
    private GoalStatus status;
}
