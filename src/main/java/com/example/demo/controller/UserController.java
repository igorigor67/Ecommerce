package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@Valid @RequestBody UserModel userModel){
        return userService.register(userModel);
    }

    @GetMapping("getUser/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("updateUser/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @Valid @RequestBody UserModel userModel){
        return userService.updateUser(userId,userModel);
    }

    @DeleteMapping("deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.verify(userModel));
    }


}
