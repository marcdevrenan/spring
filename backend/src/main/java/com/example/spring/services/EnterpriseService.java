package com.example.spring.services;

import com.example.spring.models.Enterprise;
import com.example.spring.repositories.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Transactional(readOnly = true)
    public List<Enterprise> findAll() {
        return enterpriseRepository.findAll();
    }
}
