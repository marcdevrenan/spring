package com.example.spring.services;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.factories.EnterpriseFactory;
import com.example.spring.models.Enterprise;
import com.example.spring.repositories.EnterpriseRepository;
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
public class EnterpriseServiceIntegrationTests {

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    private Long existingId;
    private Long nonExistingId;
    private long countTotalEnterprises;
    private Enterprise enterprise;
    private EnterpriseDTO enterpriseDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalEnterprises = 3L;
        enterprise = EnterpriseFactory.createEnterprise();
        enterpriseDTO = EnterpriseFactory.createEnterpriseDTO();
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<EnterpriseDTO> result = enterpriseService.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.size(), enterpriseRepository.count());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        EnterpriseDTO result = enterpriseService.findById(enterpriseDTO.getId());

        // Assert
        Assertions.assertEquals(enterpriseDTO.getName(), result.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            enterpriseService.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnEnterpriseDTOCreated() {

        // Act
        enterpriseService.insert(enterpriseDTO);
        Optional<Enterprise> result = enterpriseRepository.findById(enterpriseDTO.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(enterpriseDTO.getName(), result.get().getName());
    }

    @Test
    public void updateShouldReturnEnterpriseDTOWhenIdExists() {

        // Act
        enterpriseService.update(enterprise.getId(), enterpriseDTO);
        Optional<Enterprise> result = enterpriseRepository.findById(enterprise.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(enterpriseDTO.getName(), result.get().getName());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        enterpriseService.delete(existingId);

        // Assert
        Assertions.assertEquals(countTotalEnterprises - 1, enterpriseRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            enterpriseService.delete(nonExistingId);
        });
    }
}
