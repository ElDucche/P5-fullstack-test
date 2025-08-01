package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password123");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testValidUser() {
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void testEmailValidation_InvalidEmail() {
        // Given
        user.setEmail("invalid-email");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("doit être une adresse électronique syntaxiquement correcte");
    }

    @Test
    void testEmailSizeValidation() {
        // Given
        user.setEmail("a".repeat(50) + "@test.com"); // Plus de 50 caractères

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 50");
    }

    @Test
    void testLastNameSizeValidation() {
        // Given
        user.setLastName("a".repeat(25)); // Plus de 20 caractères

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 20");
    }

    @Test
    void testFirstNameSizeValidation() {
        // Given
        user.setFirstName("a".repeat(25)); // Plus de 20 caractères

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 20");
    }

    @Test
    void testPasswordSizeValidation() {
        // Given
        user.setPassword("a".repeat(125)); // Plus de 120 caractères

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 120");
    }

    @Test
    void testGettersAndSetters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        User testUser = new User();

        // When
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setLastName("Doe");
        testUser.setFirstName("John");
        testUser.setPassword("password123");
        testUser.setAdmin(true);
        testUser.setCreatedAt(now);
        testUser.setUpdatedAt(now);

        // Then
        assertThat(testUser.getId()).isEqualTo(1L);
        assertThat(testUser.getEmail()).isEqualTo("test@example.com");
        assertThat(testUser.getLastName()).isEqualTo("Doe");
        assertThat(testUser.getFirstName()).isEqualTo("John");
        assertThat(testUser.getPassword()).isEqualTo("password123");
        assertThat(testUser.isAdmin()).isTrue();
        assertThat(testUser.getCreatedAt()).isEqualTo(now);
        assertThat(testUser.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testRequiredArgsConstructor() {
        // Given
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";
        boolean admin = true;

        // When
        User testUser = new User(email, lastName, firstName, password, admin);

        // Then
        assertThat(testUser.getEmail()).isEqualTo(email);
        assertThat(testUser.getLastName()).isEqualTo(lastName);
        assertThat(testUser.getFirstName()).isEqualTo(firstName);
        assertThat(testUser.getPassword()).isEqualTo(password);
        assertThat(testUser.isAdmin()).isEqualTo(admin);
        assertThat(testUser.getId()).isNull();
        assertThat(testUser.getCreatedAt()).isNull();
        assertThat(testUser.getUpdatedAt()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";
        boolean admin = false;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        User testUser = new User(id, email, lastName, firstName, password, admin, createdAt, updatedAt);

        // Then
        assertThat(testUser.getId()).isEqualTo(id);
        assertThat(testUser.getEmail()).isEqualTo(email);
        assertThat(testUser.getLastName()).isEqualTo(lastName);
        assertThat(testUser.getFirstName()).isEqualTo(firstName);
        assertThat(testUser.getPassword()).isEqualTo(password);
        assertThat(testUser.isAdmin()).isEqualTo(admin);
        assertThat(testUser.getCreatedAt()).isEqualTo(createdAt);
        assertThat(testUser.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        User testUser = new User();

        // Then
        assertThat(testUser.getId()).isNull();
        assertThat(testUser.getEmail()).isNull();
        assertThat(testUser.getLastName()).isNull();
        assertThat(testUser.getFirstName()).isNull();
        assertThat(testUser.getPassword()).isNull();
        assertThat(testUser.isAdmin()).isFalse(); // Default value for boolean
        assertThat(testUser.getCreatedAt()).isNull();
        assertThat(testUser.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderPattern() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        User testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(testUser.getId()).isEqualTo(1L);
        assertThat(testUser.getEmail()).isEqualTo("test@example.com");
        assertThat(testUser.getLastName()).isEqualTo("Doe");
        assertThat(testUser.getFirstName()).isEqualTo("John");
        assertThat(testUser.getPassword()).isEqualTo("password123");
        assertThat(testUser.isAdmin()).isTrue();
        assertThat(testUser.getCreatedAt()).isEqualTo(now);
        assertThat(testUser.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testBuilderMinimalFields() {
        // When
        User testUser = User.builder()
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        // Then
        assertThat(testUser.getEmail()).isEqualTo("test@example.com");
        assertThat(testUser.getLastName()).isEqualTo("Doe");
        assertThat(testUser.getFirstName()).isEqualTo("John");
        assertThat(testUser.getPassword()).isEqualTo("password123");
        assertThat(testUser.isAdmin()).isFalse();
        assertThat(testUser.getId()).isNull();
        assertThat(testUser.getCreatedAt()).isNull();
        assertThat(testUser.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderEdgeCases() {
        // Given & When - Test builder with null values (except required ones)
        User userWithNulls = User.builder()
                .id(null)
                .email("nulltest@example.com")
                .lastName("NullTest")
                .firstName("User")
                .password("password123")
                .admin(false)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(userWithNulls.getId()).isNull();
        assertThat(userWithNulls.getEmail()).isEqualTo("nulltest@example.com");
        assertThat(userWithNulls.getCreatedAt()).isNull();
        assertThat(userWithNulls.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderStepByStep() {
        // Given
        User.UserBuilder builder = User.builder();

        // When - Build step by step
        User stepUser = builder
                .id(10L)
                .email("step@example.com")
                .firstName("Step")
                .lastName("Builder")
                .password("steppass")
                .admin(true)
                .build();

        // Then
        assertThat(stepUser.getId()).isEqualTo(10L);
        assertThat(stepUser.getEmail()).isEqualTo("step@example.com");
        assertThat(stepUser.getFirstName()).isEqualTo("Step");
        assertThat(stepUser.getLastName()).isEqualTo("Builder");
        assertThat(stepUser.getPassword()).isEqualTo("steppass");
        assertThat(stepUser.isAdmin()).isTrue();
    }

    @Test
    void testBuilderReuseability() {
        // Given
        User.UserBuilder baseBuilder = User.builder()
                .email("base@example.com")
                .firstName("Base")
                .lastName("Template")
                .password("basepass")
                .admin(false);

        // When - Reuse builder with different IDs
        User user1 = baseBuilder.id(1L).build();
        User user2 = baseBuilder.id(2L).build();

        // Then
        assertThat(user1.getId()).isEqualTo(1L);
        assertThat(user2.getId()).isEqualTo(2L);
        assertThat(user1.getEmail()).isEqualTo("base@example.com");
        assertThat(user2.getEmail()).isEqualTo("base@example.com");
    }

    @Test
    void testBuilderAccessorsChaining() {
        // Given
        User originalUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        // When - Test chaining with modification
        User modifiedUser = originalUser
                .setEmail("modified@example.com")
                .setAdmin(true);

        // Then
        assertThat(modifiedUser.getId()).isEqualTo(1L);
        assertThat(modifiedUser.getEmail()).isEqualTo("modified@example.com");
        assertThat(modifiedUser.getLastName()).isEqualTo("Doe");
        assertThat(modifiedUser.getFirstName()).isEqualTo("John");
        assertThat(modifiedUser.getPassword()).isEqualTo("password123");
        assertThat(modifiedUser.isAdmin()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("different@example.com")
                .lastName("Smith")
                .firstName("Jane")
                .password("differentPassword")
                .admin(true)
                .build();

        User user3 = User.builder()
                .id(2L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();

        // Then - EqualsAndHashCode is based on "id" only
        assertThat(user1)
                .isEqualTo(user2) // Same ID
                .isNotEqualTo(user3) // Different ID
                .hasSameHashCodeAs(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void testToString() {
        // When
        String toString = user.toString();

        // Then
        assertThat(toString)
                .contains("User")
                .contains("id=1")
                .contains("email=test@example.com")
                .contains("lastName=Doe")
                .contains("firstName=John")
                .contains("admin=false");
    }

    @Test
    void testValidationWithInvalidEmail() {
        // Given - tester seulement la validation email qui existe vraiment
        User testUser = new User();
        testUser.setEmail("invalid-email"); // Email invalide
        testUser.setLastName("Doe"); 
        testUser.setFirstName("John"); 
        testUser.setPassword("password123"); 
        testUser.setAdmin(false);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        // Then
        // Devrait avoir 1 violation : email invalide
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath()).hasToString("email");
    }

    @Test
    void testAdminDefaultValue() {
        // Given
        User testUser = new User();

        // Then
        assertThat(testUser.isAdmin()).isFalse(); // Default boolean value
    }
}
