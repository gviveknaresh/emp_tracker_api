package com.emp_managment.emp_tracker.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EmployeeDTO implements Serializable {

    @NotBlank(message = "Employee code is required")
    @Size(max = 100, message = "Employee code cannot exceed 100 characters")
    private String employeeCode;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department cannot exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Role cannot exceed 100 characters")
    private String role;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Active status must be provided")
    private Boolean isActive;
}