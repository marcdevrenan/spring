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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll() {
        List<Employee> list = employeeRepository.findAll();

        return list.stream().map(e -> new EmployeeDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO findById(Long id) {
        Optional<Employee> obj = employeeRepository.findById(id);
        Employee employee = obj.orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));

        return new EmployeeDTO(employee, employee.getDepartments());
    }

    @Transactional
    public EmployeeDTO insert(EmployeeDTO dto) {
        Employee employee = new Employee();
        copyDtoToEntity(dto, employee);
        employee = employeeRepository.save(employee);

        return new EmployeeDTO(employee);
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        try {
            Employee employee = employeeRepository.getReferenceById(id);
            copyDtoToEntity(dto, employee);
            employee = employeeRepository.save(employee);

            return new EmployeeDTO(employee);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity with id " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));
            employeeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }

    private void copyDtoToEntity(EmployeeDTO dto, Employee employee) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setAge(dto.getAge());
        employee.setPosition(dto.getPosition());
        employee.setEmail(dto.getEmail());
        employee.getDepartments().clear();
        for (DepartmentDTO dptDto : dto.getDepartments()) {
            Department department = departmentRepository.getReferenceById(dptDto.getId());
            employee.getDepartments().add(department);
        }
    }
}
