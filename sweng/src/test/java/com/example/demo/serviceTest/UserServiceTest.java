package com.example.demo.serviceTest;

import com.example.demo.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private CustomUserDetailsService userService;

    @Test
    public void testExistsByFirstName() {
        boolean exists = userService.existsUserByFirstName("Davide");
        assertTrue(exists, "No users found with first name 'test'");
    }
}