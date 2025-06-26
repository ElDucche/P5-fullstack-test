package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto = new TeacherDto(1L, "Doe", "John", now, now);
        Teacher entity = teacherMapper.toEntity(dto);
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        Teacher entity = new Teacher(1L, "Doe", "John", now, now);
        TeacherDto dto = teacherMapper.toDto(entity);
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto dto2 = new TeacherDto(2L, "Smith", "Jane", now, now);
        List<TeacherDto> dtoList = new ArrayList<>();
        dtoList.add(dto1);
        dtoList.add(dto2);

        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        assertThat(entityList).isNotNull().hasSize(2);
        assertThat(entityList.get(0).getId()).isEqualTo(1L);
        assertThat(entityList.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testToDtoList() {
        LocalDateTime now = LocalDateTime.now();
        Teacher entity1 = new Teacher(1L, "Doe", "John", now, now);
        Teacher entity2 = new Teacher(2L, "Smith", "Jane", now, now);
        List<Teacher> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);

        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        assertThat(dtoList).isNotNull().hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(1L);
        assertThat(dtoList.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testToEntityList_withNullList() {
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);
        assertThat(entityList).isNull();
    }

    @Test
    void testToDtoList_withNullList() {
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);
        assertThat(dtoList).isNull();
    }

    @Test
    void testToEntity_withNullDto() {
        Teacher entity = teacherMapper.toEntity((TeacherDto) null);
        assertThat(entity).isNull();
    }

    @Test
    void testToDto_withNullEntity() {
        TeacherDto dto = teacherMapper.toDto((Teacher) null);
        assertThat(dto).isNull();
    }
}
