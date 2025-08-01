package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testValidSignupRequest() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.getEmail()).isEqualTo("test@example.com");
        assertThat(request.getFirstName()).isEqualTo("John");
        assertThat(request.getLastName()).isEqualTo("Doe");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    void testEmailValidation() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("invalid-email");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("doit être une adresse électronique syntaxiquement correcte");
    }

    @Test
    void testEmailCannotBeBlank() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testEmailSizeValidation() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("a".repeat(50) + "@test.com"); // Plus de 50 caractères
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 50");
    }

    @Test
    void testFirstNameCannotBeBlank() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName(null); // Use null instead of empty string to only trigger @NotBlank
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testFirstNameSizeValidation() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("a".repeat(25)); // Plus de 20 caractères
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 3 et 20");
    }

    @Test
    void testFirstNameMinimumSize() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("ab"); // Moins de 3 caractères
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 3 et 20");
    }

    @Test
    void testLastNameCannotBeBlank() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName(null); // Use null instead of empty string to only trigger @NotBlank
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testLastNameSizeValidation() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("a".repeat(25)); // Plus de 20 caractères
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 3 et 20");
    }

    @Test
    void testLastNameMinimumSize() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("ab"); // Moins de 3 caractères
        request.setPassword("password123");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 3 et 20");
    }

    @Test
    void testPasswordCannotBeBlank() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword(null); // Use null instead of empty string to only trigger @NotBlank

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testPasswordSizeValidation() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("a".repeat(45)); // Plus de 40 caractères

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 6 et 40");
    }

    @Test
    void testPasswordMinimumSize() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("12345"); // Moins de 6 caractères

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 6 et 40");
    }

    @Test
    void testAllFieldsInvalid() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("invalid-email");
        request.setFirstName("");
        request.setLastName("");
        request.setPassword("");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        // Each empty field triggers both @NotBlank and @Size violations + @Email for email
        assertThat(violations).hasSize(7); // 1 email + 2 firstName + 2 lastName + 2 password
    }

    @Test
    void testLombokGeneratedMethods() {
        // Given
        SignupRequest request = new SignupRequest();
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";

        // When
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPassword(password);

        // Then
        assertThat(request.getEmail()).isEqualTo(email);
        assertThat(request.getFirstName()).isEqualTo(firstName);
        assertThat(request.getLastName()).isEqualTo(lastName);
        assertThat(request.getPassword()).isEqualTo(password);
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        // Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("test@example.com");
        assertThat(toString).contains("John");
        assertThat(toString).contains("Doe");
        assertThat(toString).contains("password123");
    }
}
