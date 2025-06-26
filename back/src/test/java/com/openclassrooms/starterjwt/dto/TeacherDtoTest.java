package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TeacherDtoTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto = new TeacherDto(
                1L,
                "Doe",
                "John",
                now,
                now
        );
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSetters() {
        TeacherDto dto = new TeacherDto();
        LocalDateTime now = LocalDateTime.now();
        dto.setId(2L);
        dto.setLastName("Last");
        dto.setFirstName("First");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getLastName()).isEqualTo("Last");
        assertThat(dto.getFirstName()).isEqualTo("First");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
}
