package com.school.users.controllerTest;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.users.controller.UserController;
import com.school.users.entity.UserEntity;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    @Autowired
    private UserRepository repository;

    @Autowired
    ObjectMapper mapper;

   @InjectMocks
    private UserController controller;

    @InjectMocks
    private UserService service;

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
        Mockito.when(repository.findAll()).thenReturn(allUsers);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].username", is("MinecraftSteve")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsersEmptyList() throws Exception {
        List<UserEntity> allUsers = new ArrayList<>(List.of());
        Mockito.when(repository.findAll()).thenReturn(allUsers);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .username("Minecraft")
                .password("aiaiai")
                .build();
        Mockito.when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdNoIdFound() throws Exception {
        long id = 2;
        UserEntity user = UserEntity.builder()
                .username("Mal")
                .password("Dito")
                .build();
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
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
        Mockito.when(repository.save(newUser)).thenReturn(newUser);
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
        Mockito.when(repository.save(newUser)).thenReturn(newUser);
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
        long userId = 1L;
        UserEntity user1 = UserEntity.builder()
                .id(userId)
                .username("Minecraft")
                .password("aiaiai")
                .build();
        UserEntity updatedUser = UserEntity.builder()
                .id(userId)
                .username("Dave")
                .password("YOLO")
                .build();
        Mockito.when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(repository.save(updatedUser)).thenReturn(updatedUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Dave")))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserNullId() throws Exception {
        long id = 2L;
        UserEntity updateUser = UserEntity.builder()
                .username("Dave")
                .password("aiaiai")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(repository.save(updateUser)).thenReturn(updateUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithNullUserBody() throws Exception {
        long id = 2L;
        UserEntity updateUser = UserEntity.builder()
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(repository.save(updateUser)).thenReturn(updateUser);
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
        long id = 1L;
        UserEntity updateUser = UserEntity.builder()
                .id(3L)
                .username("Dave")
                .password("aiaiai")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(repository.save(updateUser)).thenReturn(updateUser);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchUpdateSuccess() throws Exception {
        long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .username("Minecraft")
                .password("aiaiai")
                .build();
        UserEntity patchUpdatedUser = UserEntity.builder()
                .id(userId)
                .username("Dave")
                .build();
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(patchUpdatedUser)).thenReturn(patchUpdatedUser);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .patch("/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(patchUpdatedUser));
        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username", is("Dave")))
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchUpdateNullBody() throws Exception {
        long userId = 1L;
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();

        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .patch("/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("");

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
        Mockito.when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
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
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete("/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(deleteUser));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
}



