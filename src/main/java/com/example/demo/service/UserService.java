package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.user.ContactNumberAlreadyInUseException;
import com.example.demo.exception.user.EmailAlreadyInUseException;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.exception.user.UsernameAlreadyInUseException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserModel;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager auth;
    private final BCryptPasswordEncoder encoder;
    private final JWTService jwtService;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, AuthenticationManager auth, BCryptPasswordEncoder encoder, JWTService jwtService, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.auth = auth;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.cartRepository = cartRepository;
    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setContactNumber(user.getContactNumber());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setRole(user.getRole());
            userDto.setUsername(user.getUsername());
            userDtoList.add(userDto);
        }

        return ResponseEntity.ok(userDtoList);
    }


    public ResponseEntity<String> register(UserModel userModel) {

        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setUsername(userModel.getUsername());
        user.setEmail(userModel.getEmail());
        user.setContactNumber(userModel.getContactNumber());
        user.setRole(Role.ROLE_USER);
        user.setPassword(encoder.encode(userModel.getPassword()));



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
        Cart cart = new Cart();
        cart.setCreatedAt(new Date(System.currentTimeMillis()));
        cart.setUpdatedAt(new Date(System.currentTimeMillis()));
        cart.setUser(user);
        cartRepository.save(cart);


        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }

    public ResponseEntity<UserModel> getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        UserModel userModel = UserMapper.entityToModel(user);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }


    public ResponseEntity<String> updateUser(Long id, UserModel userModel) {
        User existingUser  = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User  not found."));

        if (userRepository.existsByContactNumber(userModel.getContactNumber()) &&
                !existingUser.getContactNumber().equals(userModel.getContactNumber())) {
            throw new ContactNumberAlreadyInUseException("Contact number already in use.");
        }
        if (userRepository.existsByEmail(userModel.getEmail()) &&
                !existingUser.getEmail().equals(userModel.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        if (userRepository.existsByUsername(userModel.getUsername()) &&
                !existingUser.getUsername().equals(userModel.getUsername())) {
            throw new UsernameAlreadyInUseException("Username already in use.");
        }

        existingUser.setUsername(userModel.getUsername());
        existingUser.setEmail(userModel.getEmail());
        existingUser.setFirstName(userModel.getFirstName());
        existingUser.setLastName(userModel.getLastName());
        existingUser.setPassword(encoder.encode(userModel.getPassword()));
        existingUser.setContactNumber(userModel.getContactNumber());
        userRepository.save(existingUser);

        return ResponseEntity.ok("Updated.");
    }

    public ResponseEntity<String> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user);

        if(cart != null)
            cartRepository.delete(cart);

        userRepository.delete(user);

        return ResponseEntity.ok("User deleted.");
    }

    public String verify(UserModel userModel) {
        Authentication authentication =
                auth.authenticate(new UsernamePasswordAuthenticationToken(
                        userModel.getUsername(),
                        userModel.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(userModel.getUsername());

        return "Fail";
    }
}
