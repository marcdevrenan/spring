package com.example.spring.factories;

import com.example.spring.dto.EmployeeDTO;
import com.example.spring.models.Employee;

public class EmployeeFactory {

    public static Employee createEmployee() {
        Employee employee = new Employee(1L, "John", "Crud", 33, "Backend Developer", "john.crud@spring.com");

        return employee;
    }

    public static EmployeeDTO createEmployeeDTO() {
        Employee employee = createEmployee();

        return new EmployeeDTO(employee);
    }
}
