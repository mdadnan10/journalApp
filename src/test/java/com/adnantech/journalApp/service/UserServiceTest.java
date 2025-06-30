package com.adnantech.journalApp.service;

import com.adnantech.journalApp.entity.User;
import com.adnantech.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveNewUser() {
        User user = new User();
        user.setUserName("adnan");
        user.setPassword("rawPassword");
        user.setRoles(List.of("USER"));

        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode("rawPassword")).thenReturn(encodedPassword);

        userService.saveUser(user);

        assertEquals("adnan", user.getUserName());
        assertEquals(List.of("USER"), user.getRoles());
        verify(userRepository, times(1)).save(user);

    }

}
