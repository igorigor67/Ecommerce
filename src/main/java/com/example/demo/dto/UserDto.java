package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "Username is required.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
    private String username;


    @Email(message = "Invalid email.")
    @NotBlank(message = "Email is required.")
    private String email;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Contact number is required.")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Contact number must be between 10 and 15 digits and can optionally start with a '+'")
    private String contactNumber;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
