package com.emp_managment.emp_tracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        name = "employees",
        indexes = @Index(name = "idx_employees_department", columnList = "department")
)
@Getter
@NoArgsConstructor
public class EmployeeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false, unique = true, length = 100)
    private String employeeCode;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(length = 100)
    private String role;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @JsonProperty
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PerformanceReviewEntity> reviews;

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}