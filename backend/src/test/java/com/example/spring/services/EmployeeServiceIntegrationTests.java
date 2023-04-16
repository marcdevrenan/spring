package com.example.spring.services;

import com.example.spring.dto.EmployeeDTO;
import com.example.spring.factories.EmployeeFactory;
import com.example.spring.models.Employee;
import com.example.spring.repositories.EmployeeRepository;
import com.example.spring.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class EmployeeServiceIntegrationTests {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    private Long existingId;
    private Long nonExistingId;
    private long countTotalEmployees;
    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalEmployees = 3L;
        employee = EmployeeFactory.createEmployee();
        employeeDTO = EmployeeFactory.createEmployeeDTO();
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<EmployeeDTO> result = employeeService.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.size(), employeeRepository.count());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        EmployeeDTO result = employeeService.findById(employeeDTO.getId());

        // Assert
        Assertions.assertEquals(employeeDTO.getFirstName(), result.getFirstName());
        Assertions.assertEquals(employeeDTO.getPosition(), result.getPosition());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnEmployeeDTOCreated() {

        // Act
        employeeService.insert(employeeDTO);
        Optional<Employee> result = employeeRepository.findById(employeeDTO.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(employeeDTO.getFirstName(), result.get().getFirstName());
        Assertions.assertEquals(employeeDTO.getPosition(), result.get().getPosition());
    }

    @Test
    public void updateShouldReturnEmployeeDTOWhenIdExists() {

        // Act
        employeeService.update(employee.getId(), employeeDTO);
        Optional<Employee> result = employeeRepository.findById(employee.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(employeeDTO.getFirstName(), result.get().getFirstName());
        Assertions.assertEquals(employeeDTO.getPosition(), result.get().getPosition());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        employeeService.delete(existingId);

        // Assert
        Assertions.assertEquals(countTotalEmployees -1, employeeRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.delete(nonExistingId);
        });
    }
}
