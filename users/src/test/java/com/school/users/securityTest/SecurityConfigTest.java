package com.school.users.securityTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testPublicEndpointAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/endpoint"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testSecureEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/endpoint"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}

