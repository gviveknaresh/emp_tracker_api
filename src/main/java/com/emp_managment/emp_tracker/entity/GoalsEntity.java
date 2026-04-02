package com.emp_managment.emp_tracker.entity;

import com.emp_managment.emp_tracker.enums.GoalStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "goals",
        indexes = {
                @Index(name = "idx_goals_cycle", columnList = "cycle_id"),
                @Index(name = "idx_goals_cycle_status", columnList = "cycle_id, status")
        }
)
@Getter
@NoArgsConstructor
public class GoalsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    @JsonIgnore
    private ReviewCycleEntity cycle;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private GoalStatus status;


    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public void setCycle(ReviewCycleEntity cycle) {
        this.cycle = cycle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }
}