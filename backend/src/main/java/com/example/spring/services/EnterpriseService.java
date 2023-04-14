package com.example.spring.services;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.models.Enterprise;
import com.example.spring.repositories.EnterpriseRepository;
import com.example.spring.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Transactional(readOnly = true)
    public List<EnterpriseDTO> findAll() {
        List<Enterprise> list = enterpriseRepository.findAll();
        return list.stream().map(e -> new EnterpriseDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnterpriseDTO findById(Long id) {
        Optional<Enterprise> obj = enterpriseRepository.findById(id);
        Enterprise enterprise = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new EnterpriseDTO(enterprise);
    }
}
