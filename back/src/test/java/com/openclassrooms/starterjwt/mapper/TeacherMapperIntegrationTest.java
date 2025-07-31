package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
class TeacherMapperIntegrationTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void toEntity_shouldMapTeacherDtoToTeacher() {
        // Given
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // When
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Then
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(teacher.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
    }

    @Test
    void toEntity_shouldReturnNullForNullInput() {
        // When
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        // Then
        assertThat(teacher).isNull();
    }

    @Test
    void toDto_shouldMapTeacherToTeacherDto() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        // When
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Then
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(teacherDto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(teacherDto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        // When
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        // Then
        assertThat(teacherDto).isNull();
    }

    @Test
    void toEntity_shouldMapListOfTeacherDtoToListOfTeacher() {
        // Given
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        // When
        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        // Then
        assertThat(teachers).hasSize(2);
        assertThat(teachers.get(0).getId()).isEqualTo(1L);
        assertThat(teachers.get(0).getFirstName()).isEqualTo("John");
        assertThat(teachers.get(0).getLastName()).isEqualTo("Doe");
        assertThat(teachers.get(1).getId()).isEqualTo(2L);
        assertThat(teachers.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(teachers.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void toDto_shouldMapListOfTeacherToListOfTeacherDto() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // When
        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        // Then
        assertThat(teacherDtos).hasSize(2);
        assertThat(teacherDtos.get(0).getId()).isEqualTo(1L);
        assertThat(teacherDtos.get(0).getFirstName()).isEqualTo("John");
        assertThat(teacherDtos.get(0).getLastName()).isEqualTo("Doe");
        assertThat(teacherDtos.get(1).getId()).isEqualTo(2L);
        assertThat(teacherDtos.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(teacherDtos.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void toEntity_shouldReturnNullForNullList() {
        // When
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertThat(teachers).isNull();
    }

    @Test
    void toDto_shouldReturnNullForNullList() {
        // When
        List<TeacherDto> teacherDtos = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertThat(teacherDtos).isNull();
    }
}
