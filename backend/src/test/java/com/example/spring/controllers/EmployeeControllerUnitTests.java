package com.example.spring.controllers;

import com.example.spring.dto.EmployeeDTO;
import com.example.spring.factories.EmployeeFactory;
import com.example.spring.services.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existingId;
    private Long nonExistingId;
    private Long tracedId;
    private EmployeeDTO employeeDTO;
    private List<EmployeeDTO> list;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 99L;
        tracedId = 2L;
        employeeDTO = EmployeeFactory.createEmployeeDTO();
        list = new ArrayList<>(List.of(employeeDTO));

        Mockito.when(employeeService.findAll()).thenReturn(list);

        Mockito.when(employeeService.findById(existingId)).thenReturn(employeeDTO);
        Mockito.when(employeeService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(employeeService.insert(ArgumentMatchers.any())).thenReturn(employeeDTO);

        Mockito.when(employeeService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(employeeDTO);
        Mockito.when(employeeService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(employeeService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(employeeService).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(employeeService).delete(tracedId);
    }

    @Test
    public void findAllShouldReturnObjectList() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.age").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.position").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnEmployeeDTOCreated() throws Exception{

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(employeeDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.age").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.position").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
    }

    @Test
    public void updateShouldReturnEmployeeDTOWhenIdExists() throws Exception{

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(employeeDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.age").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.position").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(employeeDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", nonExistingId)
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
                mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
