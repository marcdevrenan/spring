package com.example.spring.controllers;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.services.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/enterprises")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping
    public ResponseEntity<List<EnterpriseDTO>> findAll() {
        List<EnterpriseDTO> list = enterpriseService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EnterpriseDTO> findById(@PathVariable Long id) {
        EnterpriseDTO dto = enterpriseService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<EnterpriseDTO> insert(@RequestBody EnterpriseDTO dto) {
        dto = enterpriseService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EnterpriseDTO> findById(@PathVariable Long id, @RequestBody EnterpriseDTO dto) {
        dto = enterpriseService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enterpriseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
