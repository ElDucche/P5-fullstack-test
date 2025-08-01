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

class TeacherTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testValidTeacher() {
        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void testLastNameCannotBeBlank() {
        // Given
        teacher.setLastName("");

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testLastNameSizeValidation() {
        // Given
        teacher.setLastName("a".repeat(25)); // Plus de 20 caractères

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 20");
    }

    @Test
    void testFirstNameCannotBeBlank() {
        // Given
        teacher.setFirstName("");

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ne doit pas être vide");
    }

    @Test
    void testFirstNameSizeValidation() {
        // Given
        teacher.setFirstName("a".repeat(25)); // Plus de 20 caractères

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("la taille doit être comprise entre 0 et 20");
    }

    @Test
    void testGettersAndSetters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Teacher testTeacher = new Teacher();

        // When
        testTeacher.setId(1L);
        testTeacher.setLastName("Doe");
        testTeacher.setFirstName("John");
        testTeacher.setCreatedAt(now);
        testTeacher.setUpdatedAt(now);

        // Then
        assertThat(testTeacher.getId()).isEqualTo(1L);
        assertThat(testTeacher.getLastName()).isEqualTo("Doe");
        assertThat(testTeacher.getFirstName()).isEqualTo("John");
        assertThat(testTeacher.getCreatedAt()).isEqualTo(now);
        assertThat(testTeacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        Long id = 1L;
        String lastName = "Doe";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        Teacher testTeacher = new Teacher(id, lastName, firstName, createdAt, updatedAt);

        // Then
        assertThat(testTeacher.getId()).isEqualTo(id);
        assertThat(testTeacher.getLastName()).isEqualTo(lastName);
        assertThat(testTeacher.getFirstName()).isEqualTo(firstName);
        assertThat(testTeacher.getCreatedAt()).isEqualTo(createdAt);
        assertThat(testTeacher.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        Teacher testTeacher = new Teacher();

        // Then
        assertThat(testTeacher.getId()).isNull();
        assertThat(testTeacher.getLastName()).isNull();
        assertThat(testTeacher.getFirstName()).isNull();
        assertThat(testTeacher.getCreatedAt()).isNull();
        assertThat(testTeacher.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderPattern() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        Teacher testTeacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(testTeacher.getId()).isEqualTo(1L);
        assertThat(testTeacher.getLastName()).isEqualTo("Doe");
        assertThat(testTeacher.getFirstName()).isEqualTo("John");
        assertThat(testTeacher.getCreatedAt()).isEqualTo(now);
        assertThat(testTeacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testBuilderMinimalFields() {
        // When
        Teacher testTeacher = Teacher.builder()
                .lastName("Doe")
                .firstName("John")
                .build();

        // Then
        assertThat(testTeacher.getLastName()).isEqualTo("Doe");
        assertThat(testTeacher.getFirstName()).isEqualTo("John");
        assertThat(testTeacher.getId()).isNull();
        assertThat(testTeacher.getCreatedAt()).isNull();
        assertThat(testTeacher.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderEdgeCases() {
        // Given & When - Test builder with null values
        Teacher teacherWithNulls = Teacher.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // Then
        assertThat(teacherWithNulls.getId()).isNull();
        assertThat(teacherWithNulls.getFirstName()).isNull();
        assertThat(teacherWithNulls.getLastName()).isNull();
        assertThat(teacherWithNulls.getCreatedAt()).isNull();
        assertThat(teacherWithNulls.getUpdatedAt()).isNull();
    }

    @Test
    void testBuilderStepByStep() {
        // Given
        Teacher.TeacherBuilder builder = Teacher.builder();

        // When - Build step by step
        Teacher stepTeacher = builder
                .id(10L)
                .firstName("Step")
                .lastName("Builder")
                .build();

        // Then
        assertThat(stepTeacher.getId()).isEqualTo(10L);
        assertThat(stepTeacher.getFirstName()).isEqualTo("Step");
        assertThat(stepTeacher.getLastName()).isEqualTo("Builder");
    }

    @Test
    void testBuilderReuseability() {
        // Given
        Teacher.TeacherBuilder baseBuilder = Teacher.builder()
                .firstName("Base")
                .lastName("Template");

        // When - Reuse builder with different IDs
        Teacher teacher1 = baseBuilder.id(1L).build();
        Teacher teacher2 = baseBuilder.id(2L).build();

        // Then
        assertThat(teacher1.getId()).isEqualTo(1L);
        assertThat(teacher2.getId()).isEqualTo(2L);
        assertThat(teacher1.getFirstName()).isEqualTo("Base");
        assertThat(teacher2.getFirstName()).isEqualTo("Base");
    }

    @Test
    void testAccessorsChaining() {
        // When
        Teacher testTeacher = new Teacher()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John");

        // Then
        assertThat(testTeacher.getId()).isEqualTo(1L);
        assertThat(testTeacher.getLastName()).isEqualTo("Doe");
        assertThat(testTeacher.getFirstName()).isEqualTo("John");
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        Teacher teacher3 = Teacher.builder()
                .id(2L)
                .lastName("Doe")
                .firstName("John")
                .build();

        // Then - EqualsAndHashCode is based on "id" only
        assertThat(teacher1)
                .isEqualTo(teacher2) // Same ID
                .isNotEqualTo(teacher3) // Different ID
                .hasSameHashCodeAs(teacher2);
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
    }

    @Test
    void testToString() {
        // When
        String toString = teacher.toString();

        // Then
        assertThat(toString)
                .contains("Teacher")
                .contains("id=1")
                .contains("lastName=Doe")
                .contains("firstName=John");
    }

    @Test
    void testValidationWithNullValues() {
        // Given
        Teacher testTeacher = new Teacher();
        testTeacher.setLastName(null);
        testTeacher.setFirstName(null);

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Then
        // No violations for null values (unlike empty strings)
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidationWithBlankValues() {
        // Given
        Teacher testTeacher = new Teacher();
        testTeacher.setLastName(" ");  // Blank string
        testTeacher.setFirstName("  "); // Blank string

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(testTeacher);

        // Then
        assertThat(violations).hasSize(2);
    }

    @Test
    void testBuilderAccessorsChaining() {
        // Given
        Teacher originalTeacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        // When - Test chaining with modification
        Teacher modifiedTeacher = originalTeacher
                .setLastName("Smith")
                .setFirstName("Jane");

        // Then
        assertThat(modifiedTeacher.getId()).isEqualTo(1L);
        assertThat(modifiedTeacher.getLastName()).isEqualTo("Smith");
        assertThat(modifiedTeacher.getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testValidNameLengths() {
        // Given
        Teacher testTeacher = new Teacher();
        testTeacher.setLastName("a".repeat(20)); // Exactly 20 characters
        testTeacher.setFirstName("b".repeat(20)); // Exactly 20 characters

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(testTeacher);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void testEmptyNamesAreInvalid() {
        // Given
        Teacher testTeacher = new Teacher();
        testTeacher.setLastName("");
        testTeacher.setFirstName("");

        // When
        Set<ConstraintViolation<Teacher>> violations = validator.validate(testTeacher);

        // Then
        assertThat(violations)
                .hasSize(2)
                .allMatch(v -> v.getMessage().equals("ne doit pas être vide"));
    }

    @Test
    void testBuilderCompleteChain() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 15, 30);

        // When
        Teacher testTeacher = Teacher.builder()
                .id(100L)
                .lastName("Johnson")
                .firstName("Robert")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Then
        assertThat(testTeacher)
                .extracting(
                    Teacher::getId,
                    Teacher::getLastName,
                    Teacher::getFirstName,
                    Teacher::getCreatedAt,
                    Teacher::getUpdatedAt
                )
                .containsExactly(100L, "Johnson", "Robert", createdAt, updatedAt);
    }
}
