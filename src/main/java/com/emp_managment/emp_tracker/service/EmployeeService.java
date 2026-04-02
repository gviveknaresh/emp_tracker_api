package com.emp_managment.emp_tracker.service;

import com.emp_managment.emp_tracker.dto.EmployeeDTO;
import com.emp_managment.emp_tracker.dto.EmployeeFilterRequest;
import com.emp_managment.emp_tracker.dto.EmployeeFilterResponseDTO;
import org.springframework.data.domain.Page;

public interface EmployeeService {

    void saveEmployee(EmployeeDTO employeeDTO);

    Page<EmployeeFilterResponseDTO> filterEmployees(EmployeeFilterRequest req);
}
