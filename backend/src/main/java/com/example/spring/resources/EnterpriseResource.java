package com.example.spring.resources;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.services.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/enterprises")
public class EnterpriseResource {

    @Autowired
    private EnterpriseService enterpriseService;

//    @GetMapping
//    public ResponseEntity<Page<EnterpriseDTO>> findAll(
//            @RequestParam(value = "page", defaultValue = "0") Integer page,
//            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
//            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
//            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
//    ) {
//        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
//
//        Page<EnterpriseDTO> list = enterpriseService.findAllPaged(pageRequest);
//
//        return ResponseEntity.ok().body(list);
//    }

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
