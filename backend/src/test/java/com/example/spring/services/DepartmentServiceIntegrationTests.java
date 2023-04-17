package com.example.spring.services;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.factories.DepartmentFactory;
import com.example.spring.models.Department;
import com.example.spring.repositories.DepartmentRepository;
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
public class DepartmentServiceIntegrationTests {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;
    private Long existingId;
    private Long nonExistingId;
    private long countTotalDepartments;
    private Department department;
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalDepartments = 3L;
        department = DepartmentFactory.createDepartment();
        departmentDTO = DepartmentFactory.createDepartmentDTO();
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<DepartmentDTO> result = departmentService.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.size(), departmentRepository.count());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        DepartmentDTO result = departmentService.findById(departmentDTO.getId());

        // Assert
        Assertions.assertEquals(departmentDTO.getName(), result.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnDepartmentDTOCreated() {

        // Act
        departmentService.insert(departmentDTO);
        Optional<Department> result = departmentRepository.findById(departmentDTO.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(departmentDTO.getName(), result.get().getName());
    }

    @Test
    public void updateShouldReturnDepartmentDTOWhenIdExists() {

        // Act
        departmentService.update(department.getId(), departmentDTO);
        Optional<Department> result = departmentRepository.findById(department.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(departmentDTO.getName(), result.get().getName());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        departmentService.delete(existingId);

        // Assert
        Assertions.assertEquals(countTotalDepartments - 1, departmentRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.delete(nonExistingId);
        });
    }
}
