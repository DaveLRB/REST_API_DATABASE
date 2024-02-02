package com.school.users.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.users.controller.UserController;
import com.school.users.entity.UserEntity;
import com.school.users.exceptions.InvalidRequestException;
import com.school.users.exceptions.UserIdNotFoundException;
import com.school.users.repository.UserRepository;
import com.school.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsersSuccess() throws Exception {
        UserEntity user1 = UserEntity.builder()
                .username("Minecraft")
                .password("aiaiai")
                .build();
        UserEntity user2 = UserEntity.builder()
                .username("MinecraftSteve")
                .password("aiaiai")
                .build();
        List<UserEntity> allUsers = new ArrayList<>(Arrays.asList(user1, user2));
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].username", is("MinecraftSteve")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        UserEntity user1 = UserEntity.builder()
                .username("Minecraft")
                .password("aiaiai")
                .build();
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Minecraft")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdNoIdFound() throws Exception {
        long id = 2;
        UserEntity user = UserEntity.builder()
                .username("Mal")
                .password("Dito")
                .build();
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        UserEntity newUser = UserEntity.builder()
                .id(1L)
                .username("Dave")
                .password("YOLO")
                .build();
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newUser));
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Dave")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUserNoBody() throws Exception {
        UserEntity newUser = UserEntity.builder()
                .id(1L)
                .build();
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .username("Minecraft")
                .password("aiaiai")
                .build();
        UserEntity updatedUser = UserEntity.builder()
                .id(1L)
                .username("Dave")
                .password("YOLO")
                .build();
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Dave")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUserNullId() throws Exception {
        long id = 2;
        UserEntity updateUser = UserEntity.builder()
                .username("Dave")
                .password("aiaiai")
                .build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(updateUser)).thenReturn(updateUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        long id = 1;
        UserEntity updateUser = UserEntity.builder()
                .id(3L)
                .username("Dave")
                .password("aiaiai")
                .build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(updateUser)).thenReturn(updateUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete_Success() throws Exception {
        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .username("Diva")
                .password("Nona")
                .build();
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete_NoIdFound() throws Exception {
        long id = 2;
        UserEntity deleteUser = UserEntity.builder()
                .username("Dave")
                .password("aiaiai")
                .build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(deleteUser)).thenReturn(deleteUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(deleteUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
}
//TODO patch_success and patch_error


