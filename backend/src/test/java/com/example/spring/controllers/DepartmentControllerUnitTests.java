package com.example.spring.controllers;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.factories.DepartmentFactory;
import com.example.spring.services.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existingId;
    private Long nonExistingId;
    private Long tracedId;
    private DepartmentDTO departmentDTO;
    private List<DepartmentDTO> list;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 99L;
        tracedId = 2L;
        departmentDTO = DepartmentFactory.createDepartmentDTO();
        list = new ArrayList<>(List.of(departmentDTO));

        Mockito.when(departmentService.findAll()).thenReturn(list);

        Mockito.when(departmentService.findById(existingId)).thenReturn(departmentDTO);
        Mockito.when(departmentService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(departmentService.insert(ArgumentMatchers.any())).thenReturn(departmentDTO);

        Mockito.when(departmentService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(departmentDTO);
        Mockito.when(departmentService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(departmentService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(departmentService).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(departmentService).delete(tracedId);
    }

    @Test
    public void findAllShouldReturnObjectList() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/departments")
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findByIdShouldReturnObjectWhenIdExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void insertShouldReturnDepartmentDTOCreated() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(departmentDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/departments")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void updateShouldReturnDepartmentDTOWhenIdExists() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(departmentDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/departments/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.phone").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Arrange
        String jsonBody = objectMapper.writeValueAsString(departmentDTO);

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/departments/{id}", nonExistingId)
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
                mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        // Act
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
