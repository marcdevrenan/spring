package com.example.spring.repositories;

import com.example.spring.factories.EnterpriseFactory;
import com.example.spring.models.Enterprise;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EnterpriseRepositoryUnitTests {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    private long existingId;
    private long nonExistingId;
    private long countTotalEnterprises;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 99L;
        countTotalEnterprises = 3L;
    }

    @Test
    public void findAllShouldReturnObjectList() {

        // Act
        List<Enterprise> result = enterpriseRepository.findAll();

        // Assert
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() {

        // Act
        Optional<Enterprise> result = enterpriseRepository.findById(existingId);

        // Assert
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldNotReturnObjectWhenIdDoesNotExists() {

        // Act
        Optional<Enterprise> result = enterpriseRepository.findById(nonExistingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        // Arrange
        Enterprise enterprise = EnterpriseFactory.createEnterprise();
        enterprise.setId(null);

        // Act
        enterprise = enterpriseRepository.save(enterprise);

        // Assert
        Assertions.assertNotNull(enterprise.getId());
        Assertions.assertEquals(countTotalEnterprises + 1, enterprise.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        // Act
        enterpriseRepository.deleteById(existingId);
        Optional<Enterprise> result = enterpriseRepository.findById(existingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
