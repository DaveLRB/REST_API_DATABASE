package com.school.users.entityTest;

import com.school.users.entity.UserEntity;
import com.school.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class UserEntityTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testUserEntityConstruction() {
        String username = "testUser";
        String password = "testPassword";

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }
}
