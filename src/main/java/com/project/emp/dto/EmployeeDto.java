package com.project.emp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Department cannot be blank")
    private String department;

    @NotBlank(message = "Designation cannot be blank")
    private String designation;

    @NotNull(message = "Salary cannot be null")
    @Positive(message = "Salary must be greater than zero")
    private Double salary;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}