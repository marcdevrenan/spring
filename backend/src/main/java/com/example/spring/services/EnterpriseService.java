package com.example.spring.services;

import com.example.spring.dto.DepartmentDTO;
import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.models.Department;
import com.example.spring.models.Enterprise;
import com.example.spring.repositories.DepartmentRepository;
import com.example.spring.repositories.EnterpriseRepository;
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
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<EnterpriseDTO> findAll() {
        List<Enterprise> list = enterpriseRepository.findAll();

        return list.stream().map(e -> new EnterpriseDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnterpriseDTO findById(Long id) {
        Optional<Enterprise> obj = enterpriseRepository.findById(id);
        Enterprise enterprise = obj.orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));

        return new EnterpriseDTO(enterprise, enterprise.getDepartments());
    }

    @Transactional
    public EnterpriseDTO insert(EnterpriseDTO dto) {
        Enterprise enterprise = new Enterprise();
        copyDtoToEntity(dto, enterprise);
        enterprise = enterpriseRepository.save(enterprise);

        return new EnterpriseDTO(enterprise);
    }

    @Transactional
    public EnterpriseDTO update(Long id, EnterpriseDTO dto) {
        try {
            Enterprise enterprise = enterpriseRepository.getReferenceById(id);
            copyDtoToEntity(dto, enterprise);
            enterprise = enterpriseRepository.save(enterprise);

            return new EnterpriseDTO(enterprise);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity with id " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            enterpriseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));
            enterpriseRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }

    private void copyDtoToEntity(EnterpriseDTO dto, Enterprise enterprise) {
        enterprise.setName(dto.getName());
        enterprise.setAddress(dto.getAddress());
        enterprise.setPhone(dto.getPhone());
        enterprise.getDepartments().clear();
        for (DepartmentDTO dptDto : dto.getDepartments()) {
            Department department = departmentRepository.getReferenceById(dptDto.getId());
            enterprise.getDepartments().add(department);
        }
    }
}
