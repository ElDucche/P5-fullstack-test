package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);
    }

    @Test
    void testFindById() {
        User foundUser = userService.findById(user.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testFindById_NotFound() {
        User foundUser = userService.findById(999L);
        assertThat(foundUser).isNull();
    }

    @Test
    void testDelete() {
        Long userId = user.getId();
        userService.delete(userId);
        assertThat(userRepository.findById(userId)).isEmpty();
    }
}
