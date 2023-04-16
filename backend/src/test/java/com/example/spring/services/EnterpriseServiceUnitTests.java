package com.example.spring.services;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.factories.EnterpriseFactory;
import com.example.spring.models.Enterprise;
import com.example.spring.repositories.EnterpriseRepository;
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
public class EnterpriseServiceUnitTests {

    @InjectMocks
    private EnterpriseService enterpriseService;

    @Mock
    private EnterpriseRepository enterpriseRepository;

    private long existingId;
    private long nonExistingId;
    private Enterprise enterprise;
    private EnterpriseDTO enterpriseDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        enterprise = EnterpriseFactory.createEnterprise();
        enterpriseDTO = EnterpriseFactory.createEnterpriseDTO();

        Mockito.when(enterpriseRepository.findAll()).thenReturn(Collections.singletonList(enterprise));

        Mockito.when(enterpriseRepository.findById(existingId)).thenReturn(Optional.of(enterprise));
        Mockito.when(enterpriseRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(enterpriseRepository.save(ArgumentMatchers.any(Enterprise.class))).thenReturn(enterprise);

        Mockito.when(enterpriseRepository.getReferenceById(existingId)).thenReturn(enterprise);
        Mockito.when(enterpriseRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(enterpriseRepository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(enterpriseRepository).deleteById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<EnterpriseDTO> result = enterpriseService.findAll();

        // Assert
        Assertions.assertNotNull(result);

        Mockito.verify(enterpriseRepository, Mockito.times(1)).findAll();

    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        EnterpriseDTO result = enterpriseService.findById(existingId);

        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            enterpriseService.findById(nonExistingId);
        });

        Mockito.verify(enterpriseRepository, Mockito.times(1)).findById(nonExistingId);
    }

//    @Test
//    public void insertShouldReturnEnterpriseDTOCreated() {
//
//        // Act
//        EnterpriseDTO result = enterpriseService.insert(enterpriseDTO);
//
//        // Assert
//        Assertions.assertNotNull(result.getId());
//        Assertions.assertEquals(enterpriseDTO.getName(), result.getName());
//        Assertions.assertEquals(enterpriseDTO.getAddress(), result.getAddress());
//
//        Mockito.verify(enterpriseRepository, Mockito.times(1))
//                .save(ArgumentMatchers.any(Enterprise.class));
//    }
//
//    @Test
//    public void updateShouldReturnEnterpriseDTOWhenIdExists() {
//        // Act
//        EnterpriseDTO result = enterpriseService.update(existingId, enterpriseDTO);
//
//        // Assert
//        Assertions.assertEquals(enterprise.getId(), result.getId());
//        Assertions.assertEquals(enterpriseDTO.getName(), result.getName());
//        Assertions.assertEquals(enterpriseDTO.getAddress(), result.getAddress());
//
//        Mockito.verify(enterpriseRepository, Mockito.times(1)).getReferenceById(existingId);
//        Mockito.verify(enterpriseRepository, Mockito.times(1)).save(enterprise);
//    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            enterpriseService.update(nonExistingId, enterpriseDTO);
        });

        Mockito.verify(enterpriseRepository, Mockito.times(1)).getReferenceById(nonExistingId);
        Mockito.verify(enterpriseRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(Enterprise.class));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            enterpriseService.delete(existingId);
        });

        Mockito.verify(enterpriseRepository, Mockito.times(1)).deleteById(existingId);
    }
}
