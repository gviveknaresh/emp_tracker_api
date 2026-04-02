package com.emp_managment.emp_tracker.service;

import com.emp_managment.emp_tracker.dto.EmployeeDTO;
import com.emp_managment.emp_tracker.dto.EmployeeFilterRequest;
import com.emp_managment.emp_tracker.dto.EmployeeFilterResponseDTO;
import com.emp_managment.emp_tracker.entity.EmployeeEntity;
import com.emp_managment.emp_tracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.emp_managment.emp_tracker.constants.Constants.AVG_RATING;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    /**
     * Saving employee entity presuming employee code unique check is already done
     */
    @Override
    @Transactional
    public void saveEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists");
        }
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmployeeCode(employeeDTO.getEmployeeCode());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setName(employeeDTO.getName());
        employee.setRole(employeeDTO.getRole());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setIsActive(true);
        employeeRepository.save(employee);
    }

    /**
     * Fetch pageable employee list based on performance and department
     * using department index in employee table for quicker lookup
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeFilterResponseDTO> filterEmployees(EmployeeFilterRequest req) {

        String dept = StringUtils.hasText(req.department())
                ? req.department().trim()
                : null;

        Pageable pageable = PageRequest.of(
                req.page(),
                req.size(),
                Sort.by(Sort.Direction.DESC, AVG_RATING)
        );

        return employeeRepository
                .findByDepartmentAndMinRating(dept, req.minRating(), pageable)
                .map(EmployeeFilterResponseDTO::from);
    }
}
