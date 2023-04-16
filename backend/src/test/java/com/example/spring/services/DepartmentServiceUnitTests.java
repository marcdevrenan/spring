package com.example.spring.services;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.factories.DepartmentFactory;
import com.example.spring.models.Department;
import com.example.spring.repositories.DepartmentRepository;
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
public class DepartmentServiceUnitTests {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private long existingId;
    private long nonExistingId;
    private Department department;
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        department = DepartmentFactory.createDepartment();
        departmentDTO = DepartmentFactory.createDepartmentDTO();

        Mockito.when(departmentRepository.findAll()).thenReturn(Collections.singletonList(department));

        Mockito.when(departmentRepository.findById(existingId)).thenReturn(Optional.of(department));
        Mockito.when(departmentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(departmentRepository.save(ArgumentMatchers.any(Department.class))).thenReturn(department);

        Mockito.when(departmentRepository.getReferenceById(existingId)).thenReturn(department);
        Mockito.when(departmentRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(departmentRepository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(departmentRepository).deleteById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<DepartmentDTO> result = departmentService.findAll();

        // Assert
        Assertions.assertNotNull(result);

        Mockito.verify(departmentRepository, Mockito.times(1)).findAll();

    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        DepartmentDTO result = departmentService.findById(existingId);

        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.findById(nonExistingId);
        });

        Mockito.verify(departmentRepository, Mockito.times(1)).findById(nonExistingId);
    }

//    @Test
//    public void insertShouldReturnDepartmentDTOCreated() {
//
//        // Act
//        DepartmentDTO result = departmentService.insert(departmentDTO);
//
//        // Assert
//        Assertions.assertNotNull(result.getId());
//        Assertions.assertEquals(departmentDTO.getName(), result.getName());
//        Assertions.assertEquals(departmentDTO.getDescription(), result.getDescription());
//
//        Mockito.verify(departmentRepository, Mockito.times(1))
//                .save(ArgumentMatchers.any(Department.class));
//    }
//
//    @Test
//    public void updateShouldReturnDepartmentDTOWhenIdExists() {
//        // Act
//        DepartmentDTO result = departmentService.update(existingId, departmentDTO);
//
//        // Assert
//        Assertions.assertEquals(department.getId(), result.getId());
//        Assertions.assertEquals(departmentDTO.getName(), result.getName());
//        Assertions.assertEquals(departmentDTO.getDescription(), result.getDescription());
//
//        Mockito.verify(departmentRepository, Mockito.times(1)).getReferenceById(existingId);
//        Mockito.verify(departmentRepository, Mockito.times(1)).save(department);
//    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.update(nonExistingId, departmentDTO);
        });

        Mockito.verify(departmentRepository, Mockito.times(1)).getReferenceById(nonExistingId);
        Mockito.verify(departmentRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(Department.class));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            departmentService.delete(existingId);
        });

        Mockito.verify(departmentRepository, Mockito.times(1)).deleteById(existingId);
    }
}
