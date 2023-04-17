package com.example.spring.repositories;

import com.example.spring.factories.EmployeeFactory;
import com.example.spring.models.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryUnitTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private long existingId;
    private long nonExistingId;
    private long countTotalEmployees;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalEmployees = 3L;
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<Employee> result = employeeRepository.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        Optional<Employee> result = employeeRepository.findById(existingId);

        // Assert
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldNotReturnObjectWhenIdDoesNotExists() {

        // Act
        Optional<Employee> result = employeeRepository.findById(nonExistingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        // Arrange
        Employee employee = EmployeeFactory.createEmployee();
        employee.setId(null);

        // Act
        employee = employeeRepository.save(employee);

        // Assert
        Assertions.assertNotNull(employee.getId());
        Assertions.assertEquals(countTotalEmployees + 1, employee.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        employeeRepository.deleteById(existingId);
        Optional<Employee> result = employeeRepository.findById(existingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
