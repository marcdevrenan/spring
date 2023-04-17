package com.example.spring.repositories;

import com.example.spring.factories.DepartmentFactory;
import com.example.spring.models.Department;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class DepartmentRepositoryUnitTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    private long existingId;
    private long nonExistingId;
    private long countTotalDepartments;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalDepartments = 3L;
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<Department> result = departmentRepository.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        Optional<Department> result = departmentRepository.findById(existingId);

        // Assert
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldNotReturnObjectWhenIdDoesNotExists() {

        // Act
        Optional<Department> result = departmentRepository.findById(nonExistingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        // Arrange
        Department department = DepartmentFactory.createDepartment();
        department.setId(null);

        // Act
        department = departmentRepository.save(department);

        // Assert
        Assertions.assertNotNull(department.getId());
        Assertions.assertEquals(countTotalDepartments + 1, department.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        departmentRepository.deleteById(existingId);
        Optional<Department> result = departmentRepository.findById(existingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
