package com.example.spring.dto;

import com.example.spring.models.Department;
import com.example.spring.models.Employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DepartmentDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String phone;
    private Long enterpriseId;

    private List<EmployeeDTO> employees = new ArrayList<>();

    public DepartmentDTO() {
    }

    public DepartmentDTO(Long id, String name, String description, String phone, Long enterpriseId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.enterpriseId = enterpriseId;
    }

    public DepartmentDTO(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
        this.phone = department.getPhone();
        this.enterpriseId = department.getEnterpriseId();
    }

    public DepartmentDTO(Department department, Set<Employee> employees) {
        this(department);
        employees.forEach(e -> this.employees.add(new EmployeeDTO(e)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }
}
