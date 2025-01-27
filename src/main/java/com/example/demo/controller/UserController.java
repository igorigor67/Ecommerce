package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
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
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserModel userModel){
        return userService.register(userModel);
    }

    //getById
    @GetMapping("{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //updateUser
    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody User user){
        return userService.updateUser(id,user);
    }

    //deleteUsser
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }


}
