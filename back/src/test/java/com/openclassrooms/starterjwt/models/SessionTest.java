package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private Session session;
    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Setup Teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Setup Users
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setPassword("password123");
        user1.setAdmin(false);

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setPassword("password456");
        user2.setAdmin(false);

        // Setup Session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testValidSession() {
        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void testNameCannotBeBlank() {
        // Given
        session.setName("");

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testNameSizeValidation() {
        // Given
        session.setName("a".repeat(55)); // Plus de 50 caractères

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 50");
    }

    @Test
    void testDateCannotBeNull() {
        // Given
        session.setDate(null);

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être nul");
    }

    @Test
    void testDescriptionCannotBeNull() {
        // Given
        session.setDescription(null);

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être nul");
    }

    @Test
    void testDescriptionSizeValidation() {
        // Given
        session.setDescription("a".repeat(2505)); // Plus de 2500 caractères

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 2500");
    }

    @Test
    void testGettersAndSetters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        List<User> users = Arrays.asList(user1);
        Session testSession = new Session();

        // When
        testSession.setId(1L);
        testSession.setName("Test Session");
        testSession.setDate(date);
        testSession.setDescription("Test Description");
        testSession.setTeacher(teacher);
        testSession.setUsers(users);
        testSession.setCreatedAt(now);
        testSession.setUpdatedAt(now);

        // Then
        assertThat(testSession.getId()).isEqualTo(1L);
        assertThat(testSession.getName()).isEqualTo("Test Session");
        assertThat(testSession.getDate()).isEqualTo(date);
        assertThat(testSession.getDescription()).isEqualTo("Test Description");
        assertThat(testSession.getTeacher()).isEqualTo(teacher);
        assertThat(testSession.getUsers()).isEqualTo(users);
        assertThat(testSession.getCreatedAt()).isEqualTo(now);
        assertThat(testSession.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Session testSession = new Session();

        // Then
        assertThat(testSession.getId()).isNull();
        assertThat(testSession.getName()).isNull();
        assertThat(testSession.getDate()).isNull();
        assertThat(testSession.getDescription()).isNull();
        assertThat(testSession.getTeacher()).isNull();
        assertThat(testSession.getUsers()).isNull();
        assertThat(testSession.getCreatedAt()).isNull();
        assertThat(testSession.getUpdatedAt()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String name = "Test Session";
        Date date = new Date();
        String description = "Test Description";
        List<User> users = Arrays.asList(user1, user2);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        Session testSession = new Session(id, name, date, description, teacher, users, createdAt, updatedAt);

        // Then
        assertThat(testSession.getId()).isEqualTo(id);
        assertThat(testSession.getName()).isEqualTo(name);
        assertThat(testSession.getDate()).isEqualTo(date);
        assertThat(testSession.getDescription()).isEqualTo(description);
        assertThat(testSession.getTeacher()).isEqualTo(teacher);
        assertThat(testSession.getUsers()).isEqualTo(users);
        assertThat(testSession.getCreatedAt()).isEqualTo(createdAt);
        assertThat(testSession.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void testBuilderPattern() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        List<User> users = Arrays.asList(user1, user2);

        // When
        Session testSession = Session.builder()
                .id(1L)
                .name("Builder Session")
                .date(date)
                .description("Built with builder pattern")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(testSession.getId()).isEqualTo(1L);
        assertThat(testSession.getName()).isEqualTo("Builder Session");
        assertThat(testSession.getDate()).isEqualTo(date);
        assertThat(testSession.getDescription()).isEqualTo("Built with builder pattern");
        assertThat(testSession.getTeacher()).isEqualTo(teacher);
        assertThat(testSession.getUsers()).isEqualTo(users);
        assertThat(testSession.getCreatedAt()).isEqualTo(now);
        assertThat(testSession.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testBuilderMinimalFields() {
        // Given
        Date date = new Date();

        // When
        Session testSession = Session.builder()
                .name("Minimal Session")
                .date(date)
                .description("Minimal description")
                .build();

        // Then
        assertThat(testSession.getName()).isEqualTo("Minimal Session");
        assertThat(testSession.getDate()).isEqualTo(date);
        assertThat(testSession.getDescription()).isEqualTo("Minimal description");
        assertThat(testSession.getId()).isNull();
        assertThat(testSession.getTeacher()).isNull();
        assertThat(testSession.getUsers()).isNull();
        assertThat(testSession.getCreatedAt()).isNull();
        assertThat(testSession.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderEdgeCases() {
        // Given & When - Test builder with null values
        Session sessionWithNulls = Session.builder()
                .id(null)
                .name(null)
                .date(null)
                .description(null)
                .teacher(null)
                .users(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(sessionWithNulls.getId()).isNull();
        assertThat(sessionWithNulls.getName()).isNull();
        assertThat(sessionWithNulls.getDate()).isNull();
        assertThat(sessionWithNulls.getDescription()).isNull();
        assertThat(sessionWithNulls.getTeacher()).isNull();
        assertThat(sessionWithNulls.getUsers()).isNull();
        assertThat(sessionWithNulls.getCreatedAt()).isNull();
        assertThat(sessionWithNulls.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderStepByStep() {
        // Given
        Session.SessionBuilder builder = Session.builder();
        Date testDate = new Date();

        // When - Build step by step
        Session stepSession = builder
                .id(10L)
                .name("Step Session")
                .date(testDate)
                .description("Step by step")
                .build();

        // Then
        assertThat(stepSession.getId()).isEqualTo(10L);
        assertThat(stepSession.getName()).isEqualTo("Step Session");
        assertThat(stepSession.getDate()).isEqualTo(testDate);
        assertThat(stepSession.getDescription()).isEqualTo("Step by step");
    }

    @Test
    void testBuilderReuseability() {
        // Given
        Date baseDate = new Date();
        Session.SessionBuilder baseBuilder = Session.builder()
                .name("Base Session")
                .date(baseDate)
                .description("Base template");

        // When - Reuse builder with different IDs
        Session session1 = baseBuilder.id(1L).build();
        Session session2 = baseBuilder.id(2L).build();

        // Then
        assertThat(session1.getId()).isEqualTo(1L);
        assertThat(session2.getId()).isEqualTo(2L);
        assertThat(session1.getName()).isEqualTo("Base Session");
        assertThat(session2.getName()).isEqualTo("Base Session");
        assertThat(session1.getDate()).isEqualTo(baseDate);
        assertThat(session2.getDate()).isEqualTo(baseDate);
    }

    @Test
    void testAccessorsChaining() {
        // Given
        Date date = new Date();
        List<User> users = new ArrayList<>();

        // When
        Session testSession = new Session()
                .setId(1L)
                .setName("Chained Session")
                .setDate(date)
                .setDescription("Chained description")
                .setTeacher(teacher)
                .setUsers(users);

        // Then
        assertThat(testSession.getId()).isEqualTo(1L);
        assertThat(testSession.getName()).isEqualTo("Chained Session");
        assertThat(testSession.getDate()).isEqualTo(date);
        assertThat(testSession.getDescription()).isEqualTo("Chained description");
        assertThat(testSession.getTeacher()).isEqualTo(teacher);
        assertThat(testSession.getUsers()).isEqualTo(users);
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .build();

        Session session2 = Session.builder()
                .id(1L)
                .name("Session 2") // Different name
                .build();

        Session session3 = Session.builder()
                .id(2L)
                .name("Session 1")
                .build();

        // Then - EqualsAndHashCode is based on "id" only
        assertThat(session1)
                .isEqualTo(session2) // Same ID
                .isNotEqualTo(session3) // Different ID
                .hasSameHashCodeAs(session2);
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
    }

    @Test
    void testToString() {
        // When
        String toString = session.toString();

        // Then
        assertThat(toString)
                .contains("Session")
                .contains("id=1")
                .contains("name=Yoga Session")
                .contains("description=A relaxing yoga session");
    }

    @Test
    void testValidationMaxLengths() {
        // Given
        Session testSession = new Session();
        testSession.setName("a".repeat(50)); // Exactly 50 characters
        testSession.setDate(new Date());
        testSession.setDescription("b".repeat(2500)); // Exactly 2500 characters

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(testSession);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void testUsersListOperations() {
        // Given
        Session testSession = new Session();
        List<User> users = new ArrayList<>();
        users.add(user1);

        // When
        testSession.setUsers(users);
        testSession.getUsers().add(user2);

        // Then
        assertThat(testSession.getUsers())
                .hasSize(2)
                .containsExactly(user1, user2);
    }

    @Test
    void testEmptyUsersListIsValid() {
        // Given
        session.setUsers(new ArrayList<>());

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).isEmpty();
        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void testNullUsersListIsValid() {
        // Given
        session.setUsers(null);

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Then
        assertThat(violations).isEmpty();
        assertThat(session.getUsers()).isNull();
    }

    @Test
    void testBuilderCompleteChain() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 15, 30);
        Date sessionDate = new Date();
        List<User> participants = Arrays.asList(user1, user2);

        // When
        Session testSession = Session.builder()
                .id(100L)
                .name("Complete Session")
                .date(sessionDate)
                .description("A complete session with all fields")
                .teacher(teacher)
                .users(participants)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(testSession)
                .extracting(
                    Session::getId,
                    Session::getName,
                    Session::getDate,
                    Session::getDescription,
                    Session::getTeacher,
                    Session::getUsers,
                    Session::getCreatedAt,
                    Session::getUpdatedAt
                )
                .containsExactly(
                    100L,
                    "Complete Session",
                    sessionDate,
                    "A complete session with all fields",
                    teacher,
                    participants,
                    createdAt,
                    updatedAt
                );
    }

    @Test
    void testBuilderAccessorsChaining() {
        // Given
        Session originalSession = Session.builder()
                .id(1L)
                .name("Original Session")
                .date(new Date())
                .description("Original description")
                .build();

        // When - Test chaining with modification
        Date newDate = new Date();
        Session modifiedSession = originalSession
                .setName("Modified Session")
                .setDescription("Modified description")
                .setDate(newDate)
                .setTeacher(teacher);

        // Then
        assertThat(modifiedSession.getId()).isEqualTo(1L);
        assertThat(modifiedSession.getName()).isEqualTo("Modified Session");
        assertThat(modifiedSession.getDescription()).isEqualTo("Modified description");
        assertThat(modifiedSession.getDate()).isEqualTo(newDate);
        assertThat(modifiedSession.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void testMultipleValidationErrors() {
        // Given
        Session invalidSession = new Session();
        invalidSession.setName(""); // Blank name
        invalidSession.setDate(null); // Null date
        invalidSession.setDescription(null); // Null description

        // When
        Set<ConstraintViolation<Session>> violations = validator.validate(invalidSession);

        // Then
        assertThat(violations).hasSize(3);
    }
}
