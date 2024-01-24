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

    public void createUser(UserEntity user) {
        repository.save(user);
    }

    public void updateUser(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity userEntity = UserEntity.builder()
                    .username((user.getUsername()))
                    .password(user.getPassword())
                    .build();
            repository.save(userEntity);
        }
    }

    public void updateUsername(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(user.getUsername());
            repository.save(userEntity);
        }
    }

    public void updatePassword(Long userId, UserEntity user) {
        if (repository.existsById(userId)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(user.getPassword());
            repository.save(userEntity);
        }
    }


    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
}
