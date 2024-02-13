package com.school.users.service;

import com.school.users.entity.UserEntity;
import com.school.users.exceptions.InvalidRequestException;
import com.school.users.exceptions.UserIdNotFoundException;
import com.school.users.repository.UserRepository;
import lombok.AllArgsConstructor;
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
        if (repository.findById(userId).isEmpty()) {
            throw new UserIdNotFoundException("User ID " + userId + " doesn't exist");
        }
        return repository.findById(userId);
    }

    public UserEntity createUser(UserEntity user) {
        if (user.getUsername() == null && user.getPassword() == null) {
            throw new InvalidRequestException("Username, password must not be null");
        }
        return repository.save(user);
    }

    public UserEntity updateUser(Long userId, UserEntity user) {
        if (user == null) {
            throw new InvalidRequestException("Username and password must not be null!");
        }
        UserEntity updatedUser = repository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException("User ID " + userId + " doesn't exist"));

        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(user.getPassword());
        repository.save(updatedUser);
        return updatedUser;
    }

    public UserEntity patchUser(Long userId, UserEntity user) {
        if (user == null || user.getUsername() == null && user.getPassword() == null) {
            throw new InvalidRequestException("Username and password must not be null!");
        }
            UserEntity existingUser = repository.findById(userId)
                    .orElseThrow(()-> new UserIdNotFoundException("User with ID" + userId + " not found"));

                if (user.getUsername() != null) {
                    existingUser.setUsername(user.getUsername());
                }
                if (user.getPassword() != null) {
                    existingUser.setPassword(user.getPassword());
                }
                repository.save(existingUser);

        return existingUser;
    }

    public void deleteUser(Long userId) {
        Optional<UserEntity> userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) throw new UserIdNotFoundException("User with ID " + userId + " not found");
        repository.deleteById(userId);
    }
}

