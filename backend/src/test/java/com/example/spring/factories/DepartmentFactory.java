package com.example.spring.factories;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.models.Department;
import com.example.spring.models.Employee;

public class DepartmentFactory {

    public static Department createDepartment() {
        Department department = new Department(1L, "IT", "Lorem ipsum dolor sit amet.", "555-555-1234");
        department.getEmployees().add(new Employee(1L, "Lorem", "Ipsum", 33, "Product Owner", "lorem.ipsum@spring.com"));

        return department;
    }

    public static DepartmentDTO createDepartmentDTO() {
        Department department = createDepartment();

        return new DepartmentDTO(department, department.getEmployees());
    }
}
