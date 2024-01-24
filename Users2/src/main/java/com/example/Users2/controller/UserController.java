package com.example.Users2.controller;

import com.example.Users2.entity.UserEntity;
import com.example.Users2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/getAll")
    public List<UserEntity> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<UserEntity> getUserById(@PathVariable Long userId) {
        return service.getUserById(userId);
    }

    @PostMapping("/create")
    public void createUser(@RequestBody UserEntity user) {
        service.createUser(user);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser) {
        service.updateUser(userId, updatedUser);
    }

    @PatchMapping("/username/{userId}")
    public void updateUsername(@PathVariable Long userId, @RequestBody UserEntity updatedUsername){
        service.updateUsername(userId,updatedUsername);
    }
    @PatchMapping("/password/{userId}")
    public void updatePassword(@PathVariable Long userId, @RequestBody UserEntity updatedPassword){
        service.updatePassword(userId,updatedPassword);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }
}

