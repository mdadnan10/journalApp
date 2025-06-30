package com.adnantech.journalApp.service;

import com.adnantech.journalApp.entity.User;
import com.adnantech.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsername() {
        User mockUser = new User();
        mockUser.setUserName("adnan");
        mockUser.setPassword("encodedPassword");
        mockUser.setRoles(List.of("USER"));

        when(userRepository.findByUserName("adnan")).thenReturn(mockUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername("adnan");

        assertNotNull(userDetails);
        assertEquals("adnan", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(u -> u.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUserName("adnan");

    }

}
