package com.example.spring.services;

import com.example.spring.dto.EmployeeDTO;
import com.example.spring.factories.EmployeeFactory;
import com.example.spring.models.Employee;
import com.example.spring.repositories.EmployeeRepository;
import com.example.spring.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceUnitTests {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private long existingId;
    private long nonExistingId;
    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        employee = EmployeeFactory.createEmployee();
        employeeDTO = EmployeeFactory.createEmployeeDTO();

        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));

        Mockito.when(employeeRepository.findById(existingId)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(employee);

        Mockito.when(employeeRepository.getReferenceById(existingId)).thenReturn(employee);
        Mockito.when(employeeRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(employeeRepository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(employeeRepository).deleteById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<EmployeeDTO> result = employeeService.findAll();

        // Assert
        Assertions.assertNotNull(result);

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();

    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        EmployeeDTO result = employeeService.findById(existingId);

        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.findById(nonExistingId);
        });

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnEmployeeDTOCreated() {

        // Act
        EmployeeDTO result = employeeService.insert(employeeDTO);

        // Assert
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(employeeDTO.getFirstName(), result.getFirstName());
        Assertions.assertEquals(employeeDTO.getPosition(), result.getPosition());

        Mockito.verify(employeeRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(Employee.class));
    }

    @Test
    public void updateShouldReturnEmployeeDTOWhenIdExists() {
        // Act
        EmployeeDTO result = employeeService.update(existingId, employeeDTO);

        // Assert
        Assertions.assertEquals(employee.getId(), result.getId());
        Assertions.assertEquals(employeeDTO.getFirstName(), result.getFirstName());
        Assertions.assertEquals(employeeDTO.getPosition(), result.getPosition());

        Mockito.verify(employeeRepository, Mockito.times(1)).getReferenceById(existingId);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.update(nonExistingId, employeeDTO);
        });

        Mockito.verify(employeeRepository, Mockito.times(1)).getReferenceById(nonExistingId);
        Mockito.verify(employeeRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(Employee.class));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            employeeService.delete(existingId);
        });

        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(existingId);
    }
}
