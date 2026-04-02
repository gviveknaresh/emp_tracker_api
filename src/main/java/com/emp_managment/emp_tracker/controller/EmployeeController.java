package com.emp_managment.emp_tracker.controller;

import com.emp_managment.emp_tracker.dto.EmployeeDTO;
import com.emp_managment.emp_tracker.dto.EmployeeFilterRequest;
import com.emp_managment.emp_tracker.dto.EmployeeFilterResponseDTO;
import com.emp_managment.emp_tracker.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Save a new employee
     */
    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        log.info("Employee created successfully: {}", employeeDTO.getEmployeeCode());
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    /**
     * Fet
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeFilterResponseDTO>> getEmployees(@Valid EmployeeFilterRequest request) {
        Page<EmployeeFilterResponseDTO> result = employeeService.filterEmployees(request);
        return ResponseEntity.ok(result);
    }
}