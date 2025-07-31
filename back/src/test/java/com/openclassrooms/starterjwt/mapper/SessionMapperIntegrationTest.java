package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
class SessionMapperIntegrationTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void toEntity_shouldMapSessionDtoToSession() {
        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDescription("Test Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(2L);
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());

        // When
        Session session = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(sessionDto.getId());
        assertThat(session.getName()).isEqualTo(sessionDto.getName());
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(session.getDate()).isEqualTo(sessionDto.getDate());
        assertThat(session.getCreatedAt()).isEqualTo(sessionDto.getCreatedAt());
        assertThat(session.getUpdatedAt()).isEqualTo(sessionDto.getUpdatedAt());
    }

    @Test
    void toEntity_shouldReturnNullForNullInput() {
        // When
        Session session = sessionMapper.toEntity((SessionDto) null);

        // Then
        assertThat(session).isNull();
    }

    @Test
    void toDto_shouldMapSessionToSessionDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDescription("Test Description");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getId()).isEqualTo(session.getId());
        assertThat(sessionDto.getName()).isEqualTo(session.getName());
        assertThat(sessionDto.getDescription()).isEqualTo(session.getDescription());
        assertThat(sessionDto.getDate()).isEqualTo(session.getDate());
        assertThat(sessionDto.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(sessionDto.getCreatedAt()).isEqualTo(session.getCreatedAt());
        assertThat(sessionDto.getUpdatedAt()).isEqualTo(session.getUpdatedAt());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        // When
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        // Then
        assertThat(sessionDto).isNull();
    }

    @Test
    void toDto_shouldHandleNullTeacher() {
        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setTeacher(null);

        // When
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Then
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getTeacher_id()).isNull();
    }

    @Test
    void toEntity_shouldMapListOfSessionDtoToListOfSession() {
        // Given
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

        // When
        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        // Then
        assertThat(sessions).hasSize(2);
        assertThat(sessions.get(0).getId()).isEqualTo(1L);
        assertThat(sessions.get(0).getName()).isEqualTo("Session 1");
        assertThat(sessions.get(1).getId()).isEqualTo(2L);
        assertThat(sessions.get(1).getName()).isEqualTo("Session 2");
    }

    @Test
    void toDto_shouldMapListOfSessionToListOfSessionDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");
        session1.setTeacher(teacher);

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");
        session2.setTeacher(teacher);

        List<Session> sessions = Arrays.asList(session1, session2);

        // When
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        // Then
        assertThat(sessionDtos).hasSize(2);
        assertThat(sessionDtos.get(0).getId()).isEqualTo(1L);
        assertThat(sessionDtos.get(0).getName()).isEqualTo("Session 1");
        assertThat(sessionDtos.get(1).getId()).isEqualTo(2L);
        assertThat(sessionDtos.get(1).getName()).isEqualTo("Session 2");
    }

    @Test
    void toEntity_shouldReturnNullForNullList() {
        // When
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);

        // Then
        assertThat(sessions).isNull();
    }

    @Test
    void toDto_shouldReturnNullForNullList() {
        // When
        List<SessionDto> sessionDtos = sessionMapper.toDto((List<Session>) null);

        // Then
        assertThat(sessionDtos).isNull();
    }
}
