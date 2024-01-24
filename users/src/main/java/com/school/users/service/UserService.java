package com.school.users.service;

import com.school.users.entity.UserEntity;
import com.school.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public List<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    public Optional<UserEntity> getUserById(Long userId) {
        return repository.findById(userId);
    }

    public UserEntity createUser(UserEntity user) {
        return repository.save(user);
    }

    public void updateUser(Long userId, UserEntity updatedUser) {
        if (repository.existsById(userId)) {
            UserEntity existingUser = repository.findById(userId).orElse(null);
            if (existingUser != null) {
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                repository.save(existingUser);
            }
        }
    }

    public void updateUsername(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity existingUser = repository.findById(userId).orElse(null);
            if (existingUser != null) {
                existingUser.setUsername(user.getUsername());
                repository.save(existingUser);
            }
        }
    }
    public void updatePassword(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity existingUser = repository.findById(userId).orElse(null);
            if (existingUser != null) {
                existingUser.setPassword(user.getPassword());
                repository.save(existingUser);
            }
        }
    }


    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
}
