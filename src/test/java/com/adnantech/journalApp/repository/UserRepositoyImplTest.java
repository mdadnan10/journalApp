package com.adnantech.journalApp.repository;

import com.adnantech.journalApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class UserRepositoyImplTest {

    @Autowired
    private UserRepositoyImpl userRepositoy;

    @Test
    void testUserExists() {
        log.info("Checking user exists or not");
        List<User> userForSA = userRepositoy.getUserForSA();
        log.info("user from DB : {}", userForSA.toString());
    }
}
