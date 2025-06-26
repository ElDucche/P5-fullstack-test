package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("john.doe@test.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(true);
        dto.setPassword("secret");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.isAdmin()).isTrue();
        assertThat(dto.getPassword()).isEqualTo("secret");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSetters() {
        UserDto dto = new UserDto();
        LocalDateTime now = LocalDateTime.now();
        dto.setId(2L);
        dto.setEmail("mail@test.com");
        dto.setLastName("Last");
        dto.setFirstName("First");
        dto.setAdmin(false);
        dto.setPassword("pass");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getEmail()).isEqualTo("mail@test.com");
        assertThat(dto.getLastName()).isEqualTo("Last");
        assertThat(dto.getFirstName()).isEqualTo("First");
        assertThat(dto.isAdmin()).isFalse();
        assertThat(dto.getPassword()).isEqualTo("pass");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
}
