package com.project.emp.service;

import com.project.emp.dto.EmployeeDto;
import com.project.emp.entity.Employee;
import com.project.emp.entity.User;
import com.project.emp.repository.EmployeeRepository;
import com.project.emp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        logger.info("Attempting to create employee profile for user ID: {}", employeeDto.getUserId());
        
        User user = userRepository.findById(employeeDto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", employeeDto.getUserId());
                    return new RuntimeException("User not found");
                });

        if (employeeRepository.findByUserId(user.getId()).isPresent()) {
            logger.error("Employee profile already exists for user ID: {}", user.getId());
            throw new RuntimeException("Employee profile already exists for this user");
        }

        Employee employee = new Employee();
        employee.setDepartment(employeeDto.getDepartment());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setSalary(employeeDto.getSalary());
        employee.setUser(user);

        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Successfully created employee profile with ID: {}", savedEmployee.getId());
        
        employeeDto.setId(savedEmployee.getId());
        return employeeDto;
    }

    public EmployeeDto getEmployeeById(Long id) {
        logger.info("Fetching employee profile for ID: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with ID: {}", id);
                    return new RuntimeException("Employee not found");
                });

        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setDepartment(employee.getDepartment());
        dto.setDesignation(employee.getDesignation());
        dto.setSalary(employee.getSalary());
        dto.setUserId(employee.getUser().getId());
        
        return dto;
    }

    public List<EmployeeDto> getAllEmployees() {
        logger.info("Fetching all employee profiles");
        
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        
        for (Employee employee : employees) {
            EmployeeDto dto = new EmployeeDto();
            dto.setId(employee.getId());
            dto.setDepartment(employee.getDepartment());
            dto.setDesignation(employee.getDesignation());
            dto.setSalary(employee.getSalary());
            dto.setUserId(employee.getUser().getId());
            employeeDtos.add(dto);
        }
        
        return employeeDtos;
    }
    
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        logger.info("Updating employee profile for ID: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot update. Employee not found with ID: {}", id);
                    return new RuntimeException("Employee not found");
                });

        employee.setDepartment(employeeDto.getDepartment());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setSalary(employeeDto.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Successfully updated employee profile for ID: {}", updatedEmployee.getId());

        EmployeeDto dto = new EmployeeDto();
        dto.setId(updatedEmployee.getId());
        dto.setDepartment(updatedEmployee.getDepartment());
        dto.setDesignation(updatedEmployee.getDesignation());
        dto.setSalary(updatedEmployee.getSalary());
        dto.setUserId(updatedEmployee.getUser().getId());

        return dto;
    }

    public void deleteEmployee(Long id) {
        logger.info("Attempting to delete employee profile for ID: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot delete. Employee not found with ID: {}", id);
                    return new RuntimeException("Employee not found");
                });
        
        employeeRepository.delete(employee);
        logger.info("Successfully deleted employee profile for ID: {}", id);
    }
}