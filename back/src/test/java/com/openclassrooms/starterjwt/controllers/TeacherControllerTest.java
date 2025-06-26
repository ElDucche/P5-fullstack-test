package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TeacherControllerTest {
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;
    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherDto = new TeacherDto(1L, "Doe", "John", null, null);
    }

    @Test
    void findById_shouldReturnTeacherDto_whenFound() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        ResponseEntity<?> response = teacherController.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(teacherDto);
    }

    @Test
    void findById_shouldReturnNotFound_whenTeacherIsNull() {
        when(teacherService.findById(2L)).thenReturn(null);
        ResponseEntity<?> response = teacherController.findById("2");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = teacherController.findById("notANumber");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void findAll_shouldReturnListOfTeacherDtos() {
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> dtos = Arrays.asList(teacherDto);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtos);
        ResponseEntity<?> response = teacherController.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dtos);
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = teacherController.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(Collections.emptyList());
    }
}
