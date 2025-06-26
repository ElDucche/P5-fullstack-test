package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SessionMapperTest {
    @Mock
    private TeacherService teacherService;
    @Mock
    private UserService userService;
    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        User user = new User();
        user.setId(2L);
        Session session = new Session();
        session.setId(3L);
        session.setName("Session");
        session.setDescription("Desc");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user));
        SessionDto dto = sessionMapper.toDto(session);
        assertThat(dto.getTeacher_id()).isEqualTo(1L);
        assertThat(dto.getUsers()).containsExactly(2L);
        assertThat(dto.getDescription()).isEqualTo("Desc");
    }

    @Test
    void testToEntity() {
        SessionDto dto = new SessionDto();
        dto.setId(3L);
        dto.setName("Session");
        dto.setDescription("Desc");
        dto.setTeacher_id(1L);
        dto.setUsers(Arrays.asList(2L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        User user = new User();
        user.setId(2L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user);

        Session session = sessionMapper.toEntity(dto);

        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).containsExactly(user);
        assertThat(session.getDescription()).isEqualTo("Desc");
    }

    @Test
    void testToEntity_withNulls() {
        SessionDto dto = new SessionDto();
        dto.setTeacher_id(null);
        dto.setUsers(null);

        Session session = sessionMapper.toEntity(dto);

        assertThat(session.getTeacher()).isNull();
        assertThat(session.getUsers()).isNotNull().isEmpty();
    }

    @Test
    void testToDto_withNulls() {
        Session session = new Session();
        session.setTeacher(null);
        session.setUsers(null);

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto.getTeacher_id()).isNull();
        assertThat(dto.getUsers()).isNotNull().isEmpty();
    }
}
