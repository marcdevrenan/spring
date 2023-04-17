package com.example.spring.services;

import com.example.spring.dto.RoleDTO;
import com.example.spring.dto.UserDTO;
import com.example.spring.dto.UserMasterDTO;
import com.example.spring.models.Role;
import com.example.spring.models.User;
import com.example.spring.repositories.RoleRepository;
import com.example.spring.repositories.UserRepository;
import com.example.spring.services.exceptions.DatabaseException;
import com.example.spring.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();

        return list.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));

        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserMasterDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);

        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, user);
            user = userRepository.save(user);

            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity with id " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Entity with id " + id + " not found"));
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDto.getId());
            user.getRoles().add(role);
        }
    }
}
