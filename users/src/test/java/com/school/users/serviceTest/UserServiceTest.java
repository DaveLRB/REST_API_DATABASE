package com.school.users.serviceTest;

import com.school.users.entity.UserEntity;
import com.school.users.exceptions.UserIdNotFoundException;
import com.school.users.repository.UserRepository;
import com.school.users.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {


    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    public void testGetAllUsersSuccess()  {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("test")
                .build();
        List<UserEntity> allUsers = new ArrayList<>(Collections.singletonList(user));

        when(repository.findAll()).thenReturn(allUsers);

        List<UserEntity> usersRequested = service.getAllUsers();
        assertEquals(allUsers.size(), usersRequested.size());
    }

    @Test
    public void testGetAllUsersEmptyList() {
        List<UserEntity> allUsers = Collections.emptyList();
        when(repository.findAll()).thenReturn(allUsers);

        List<UserEntity> usersAsked = service.getAllUsers();
        assertEquals(0, usersAsked.size());
    }

    @Test
    public void testGetUserByIdSuccess() {
        long userId = 1L;
        UserEntity u = UserEntity.builder()
                .id(userId)
                .username("test")
                .build();

        when(repository.findById(userId)).thenReturn(Optional.of(u));

        Optional<UserEntity> user = service.getUserById(userId);

        assertTrue(user.isPresent());
        assertEquals(user.get().getId(), userId);
        assertEquals(user.get().getUsername(), "test");
    }

    @Test
    public void testGetUserByIdNotFound() {
        long userId = 2L;
        when(repository.findById(userId)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> service.getUserById(userId));

    }

    @Test
    public void testCreateUserSuccess() {
        UserEntity newUser = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .build();
        when(repository.save(newUser)).thenReturn(newUser);

        UserEntity createdUser = service.createUser(newUser);

        assertEquals(newUser, createdUser);

    }

    @Test
    public void testCreateUserNoBody() {
        UserEntity newUser = UserEntity.builder()
                .id(1L)
                .build();
        when(repository.save(newUser)).thenReturn(newUser);
        Assertions.assertThrows(RuntimeException.class, () -> service.createUser(newUser));
    }

    @Test
    public void testUpdateUserSuccess() {
        long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .username("Minecraft")
                .password("aiaiai")
                .build();
        UserEntity updatedUser = UserEntity.builder()
                .id(userId)
                .username("Dave")
                .password("YOLO")
                .build();

        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.save(updatedUser)).thenReturn(updatedUser);

        UserEntity finalizedUpdate = service.updateUser(userId, updatedUser);

        assertEquals(updatedUser, finalizedUpdate);
    }

    @Test
    public void testUpdateUserNullId() {

        assertThrows(UserIdNotFoundException.class, () -> service.updateUser(null, new UserEntity()));
        verify(repository, never()).findById(anyLong());
        verify(repository, never()).save(any());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserIdNotFoundException.class, () -> service.updateUser(anyLong(), new UserEntity()));
        verify(repository, never()).save(any());
    }

    @Test
    public void testDeleteUser() {
        long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .username("Mine")
                .password("aiaiai")
                .build();
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        service.deleteUser(userId);

        verify(repository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUserIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserIdNotFoundException.class, () -> service.deleteUser(anyLong()));
        verify(repository, never()).deleteById(anyLong());
    }

}
