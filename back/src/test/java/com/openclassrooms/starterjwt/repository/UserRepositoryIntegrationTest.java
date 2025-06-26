package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // given
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);
        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(user.getEmail()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testExistsByEmail() {
        // given
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);
        entityManager.persist(user);
        entityManager.flush();

        // when
        boolean exists = userRepository.existsByEmail(user.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByEmail_NotFound() {
        // when
        User found = userRepository.findByEmail("nonexistent@test.com").orElse(null);

        // then
        assertThat(found).isNull();
    }

    @Test
    public void testExistsByEmail_NotFound() {
        // when
        boolean exists = userRepository.existsByEmail("nonexistent@test.com");

        // then
        assertThat(exists).isFalse();
    }
}
