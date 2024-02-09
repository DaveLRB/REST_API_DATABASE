package com.school.users.controller;

import com.school.users.entity.UserEntity;

import com.school.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping
    public List<UserEntity> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<UserEntity> getUserById(@PathVariable Long userId) {
        return service.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.status(201).body(service.createUser(user));
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser){
        service.updateUser(userId, updatedUser);
    }

    @PatchMapping("/{userId}")
    public void patchUser(@PathVariable Long userId, @RequestBody UserEntity updated) {
        service.patchUser(userId, updated);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }
}

