package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionDtoTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        SessionDto dto = new SessionDto(
                1L,
                "Session Test",
                date,
                2L,
                "Description de test",
                Arrays.asList(10L, 20L),
                now,
                now
        );
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Session Test");
        assertThat(dto.getDate()).isEqualTo(date);
        assertThat(dto.getTeacher_id()).isEqualTo(2L);
        assertThat(dto.getDescription()).isEqualTo("Description de test");
        assertThat(dto.getUsers()).containsExactly(10L, 20L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testSetters() {
        SessionDto dto = new SessionDto();
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        dto.setId(5L);
        dto.setName("Nom");
        dto.setDate(date);
        dto.setTeacher_id(7L);
        dto.setDescription("Desc");
        dto.setUsers(Arrays.asList(1L, 2L));
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getName()).isEqualTo("Nom");
        assertThat(dto.getDate()).isEqualTo(date);
        assertThat(dto.getTeacher_id()).isEqualTo(7L);
        assertThat(dto.getDescription()).isEqualTo("Desc");
        assertThat(dto.getUsers()).containsExactly(1L, 2L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }
}
