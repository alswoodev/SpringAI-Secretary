package com.spring.ai.basic.repository;

import com.spring.ai.basic.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByEmail() {
        // given
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .name("Test User")
                .build();
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<User> foundUser = userRepository.findByEmail(email);

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        // when
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUser() {
        // given
        User user = User.builder()
                .email("newuser@example.com")
                .name("New User")
                .build();

        // when
        User savedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // then
        assertNotNull(savedUser.getUserId());
        assertEquals("newuser@example.com", savedUser.getEmail());
    }
}
