package com.example.demo.mapper;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.model.UserModel;

import java.util.Optional;

public class UserMapper {

    public static User modelToEntity(UserModel userModel){
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setContactNumber(userModel.getContactNumber());
        user.setRole(Role.ROLE_USER);

        return user;
    }

    public static UserModel entityToModel(User user){
        UserModel userModel = new UserModel();
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setContactNumber(user.getContactNumber());

        return userModel;
    }



}
