package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.user.ContactNumberAlreadyInUseException;
import com.example.demo.exception.user.EmailAlreadyInUseException;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.exception.user.UsernameAlreadyInUseException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }


    public ResponseEntity<String> register(UserModel userModel) {

        var user = UserMapper.modelToEntity(userModel);

        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        if(userRepository.existsByContactNumber(user.getContactNumber())){
            throw new ContactNumberAlreadyInUseException("Contact number already in use.");
        }
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyInUseException("Username already in use.");
        }


        userRepository.save(user);

        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }

    public ResponseEntity<UserModel> getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        UserModel userModel = UserMapper.entityToModel(user);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }


    public ResponseEntity<String> updateUser(Long id, User user) {
        User existingUser  = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User  not found."));

        if (userRepository.existsByContactNumber(user.getContactNumber()) &&
                !existingUser.getContactNumber().equals(user.getContactNumber())) {
            throw new ContactNumberAlreadyInUseException("Contact number already in use.");
        }
        if (userRepository.existsByEmail(user.getEmail()) &&
                !existingUser.getEmail().equals(user.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        if (userRepository.existsByUsername(user.getUsername()) &&
                !existingUser.getUsername().equals(user.getUsername())) {
            throw new UsernameAlreadyInUseException("Username already in use.");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());
        existingUser.setContactNumber(user.getContactNumber());
        userRepository.save(existingUser);

        return ResponseEntity.ok("Updated.");
    }

    public ResponseEntity<String> deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User not found"));

        userRepository.deleteById(id);
        return ResponseEntity.ok("Deleted.");
    }
}
