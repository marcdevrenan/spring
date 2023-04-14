package com.example.spring.resources;

import com.example.spring.models.Enterprise;
import com.example.spring.services.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/enterprises")
public class EnterpriseResource {

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping
    public ResponseEntity<List<Enterprise>> findAll() {
        List<Enterprise> list = enterpriseService.findAll();
        return ResponseEntity.ok().body(list);
    }
}