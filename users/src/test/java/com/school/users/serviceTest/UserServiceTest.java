package com.school.users.serviceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.users.controller.UserController;
import com.school.users.entity.UserEntity;
import com.school.users.exceptions.UserListNotFoundException;
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
public class UserServiceTest {

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
        UserEntity user1 = new UserEntity("Minecraft", "password");
        UserEntity user2 = new UserEntity("Minecraft2", "worn");
        UserEntity user3 = new UserEntity("MinecraftSteve", "password");

        List<UserEntity> allUsers = new ArrayList<>(Arrays.asList(user1, user2, user3));
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].username", is("MinecraftSteve")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {

        UserEntity user1 = new UserEntity(1L,"Minecraft", "password");

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.username",is("Minecraft")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        UserEntity newUser = UserEntity.builder()
                .id(1L)
                .username("Dave")
                .password("YOLO")
                .build();

        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newUser));

        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.username",is("Dave")))
                .andExpect(status().isCreated());

    }

    @Test
    public void testUpdateUserSuccess() throws Exception {

        UserEntity user1 = new UserEntity(1L,"Minecraft", "password");
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
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.username",is("Dave")))
                .andExpect(status().isCreated());
    }
    @Test
    public void testUpdateUserNullId() throws Exception {
        UserEntity updateUser = UserEntity.builder()
                .id(null)
                .username("Dave")
                .password("aiaiai")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateUser));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

    }
}
