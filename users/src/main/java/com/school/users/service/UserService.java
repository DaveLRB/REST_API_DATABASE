package com.school.users.service;

import com.school.users.entity.UserEntity;
import com.school.users.exceptions.InvalidRequestException;
import com.school.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

   /* public void updateUser(Long userId, UserEntity updatedUser)  {
        if (repository.existsById(userId)) {
            UserEntity existingUser = repository.findById(userId).orElse(null);
            if (existingUser != null) {
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                repository.save(existingUser);
            }
        }
    }*/

    public UserEntity updateUser(@RequestBody UserEntity user) throws ChangeSetPersister.NotFoundException, InvalidRequestException {
        if (user == null || user.getId() == null) {
            throw new InvalidRequestException("Username or ID must not be null!");
        }
        Optional<UserEntity> optionalUser = repository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        UserEntity existingUser = optionalUser.get();

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        return repository.save(existingUser);
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
        repository.deleteById(userId);
    }
}
