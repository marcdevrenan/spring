package com.example.spring.controllers;

import com.example.spring.dto.UserDTO;
import com.example.spring.dto.UserMasterDTO;
import com.example.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO dto = userService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody UserMasterDTO dto) {
        UserDTO finalDto = userService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(finalDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(finalDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id, @RequestBody UserDTO dto) {
        dto = userService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
