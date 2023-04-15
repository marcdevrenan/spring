package com.example.spring.services;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.dto.EmployeeDTO;
import com.example.spring.models.Department;
import com.example.spring.models.Employee;
import com.example.spring.repositories.DepartmentRepository;
import com.example.spring.repositories.EmployeeRepository;
import com.example.spring.services.exceptions.DatabaseException;
import com.example.spring.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<DepartmentDTO> findAll() {
        List<Department> list = departmentRepository.findAll();
        return list.stream().map(d -> new DepartmentDTO(d)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDTO findById(Long id) {
        Optional<Department> obj = departmentRepository.findById(id);
        Department department = obj.orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));
        return new DepartmentDTO(department, department.getEmployees());
    }

    @Transactional
    public DepartmentDTO insert(DepartmentDTO dto) {
        Department department = new Department();
        copyDtoToEntity(dto, department);
        department = departmentRepository.save(department);

        return new DepartmentDTO(department);
    }

    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        try {
            Department department = departmentRepository.getReferenceById(id);
            copyDtoToEntity(dto, department);
            department = departmentRepository.save(department);

            return new DepartmentDTO(department);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity with id " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            departmentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Entity with id " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }

    private void copyDtoToEntity(DepartmentDTO dto, Department department) {
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setPhone(dto.getPhone());
        department.getEmployees().clear();
        for (EmployeeDTO empDto : dto.getEmployees()) {
            Employee employee = employeeRepository.getReferenceById(empDto.getId());
            department.getEmployees().add(employee);
        }
    }
}
