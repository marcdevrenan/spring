package com.example.spring.controllers;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.factories.EnterpriseFactory;
import com.example.spring.services.EnterpriseService;
import com.example.spring.services.exceptions.DatabaseException;
import com.example.spring.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(EnterpriseController.class)
public class EnterpriseControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EnterpriseService enterpriseService;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existingId;
    private Long nonExistingId;
    private Long tracedId;
    private EnterpriseDTO enterpriseDTO;
    private List<EnterpriseDTO> list;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 99L;
        tracedId = 2L;
        enterpriseDTO = EnterpriseFactory.createEnterpriseDTO();
        list = new ArrayList<>(List.of(enterpriseDTO));

        Mockito.when(enterpriseService.findAll()).thenReturn(list);

        Mockito.when(enterpriseService.findById(existingId)).thenReturn(enterpriseDTO);
        Mockito.when(enterpriseService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(enterpriseService.insert(ArgumentMatchers.any())).thenReturn(enterpriseDTO);

        Mockito.when(enterpriseService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(enterpriseDTO);
        Mockito.when(enterpriseService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(enterpriseService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(enterpriseService).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(enterpriseService).delete(tracedId);
    }

    @Test
    public void findAllShouldReturnObjectList() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/enterprises")
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/enterprises/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.address").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/enterprises/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnEnterpriseDTOCreated() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(enterpriseDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/enterprises")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.address").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void updateShouldReturnEnterpriseDTOWhenIdExists() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(enterpriseDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/enterprises/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.address").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(enterpriseDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/enterprises/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/enterprises/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/enterprises/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
