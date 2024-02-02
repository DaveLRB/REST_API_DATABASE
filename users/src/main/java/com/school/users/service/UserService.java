package com.school.users.service;

import com.school.users.entity.UserEntity;
import com.school.users.exceptions.InvalidRequestException;
import com.school.users.exceptions.UserIdNotFoundException;
import com.school.users.exceptions.UserListNotFoundException;
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
            throw new UserIdNotFoundException("User ID" + userId + "doesn't exist");
        }
        return repository.findById(userId);
    }

    public UserEntity createUser(UserEntity user) {
        if (user.getUsername() == null && user.getPassword() == null) {
            throw new InvalidRequestException("Username, password must not be null!");
        }
        return repository.save(user);
    }

    public void updateUser(Long userId, UserEntity user) {
        if (user == null) {
            throw new InvalidRequestException("Username, password must not be null!");
        }
        UserEntity userEntity = repository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException("Dave"));

        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        repository.save(userEntity);
    }

    public void patchUser(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity existingUser = repository.findById(userId).orElse(null);
            if (user.getUsername() != null) {
                assert existingUser != null;
                existingUser.setUsername((user.getUsername()));
            }
            if (user.getPassword() != null) {
                assert existingUser != null;
                existingUser.setPassword((user.getPassword()));
            }
            assert existingUser != null;
            repository.save(existingUser);
        }
    }

    public void deleteUser(Long userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new UserIdNotFoundException("User ID" + userId + "doesn't exist");
        }
        repository.deleteById(userId);
    }
}
