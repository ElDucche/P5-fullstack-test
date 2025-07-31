package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {
    
    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        // Créer une implémentation concrète simple pour les tests
        teacherMapper = new TeacherMapper() {
            @Override
            public Teacher toEntity(TeacherDto dto) {
                if (dto == null) {
                    return null;
                }
                return Teacher.builder()
                        .id(dto.getId())
                        .lastName(dto.getLastName())
                        .firstName(dto.getFirstName())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .build();
            }

            @Override
            public TeacherDto toDto(Teacher entity) {
                if (entity == null) {
                    return null;
                }
                return new TeacherDto(
                        entity.getId(),
                        entity.getLastName(),
                        entity.getFirstName(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                );
            }

            @Override
            public List<Teacher> toEntity(List<TeacherDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                return dtoList.stream().map(this::toEntity).toList();
            }

            @Override
            public List<TeacherDto> toDto(List<Teacher> entityList) {
                if (entityList == null) {
                    return null;
                }
                return entityList.stream().map(this::toDto).toList();
            }
        };
    }

    @Test
    void testToEntity() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto = new TeacherDto(1L, "Doe", "John", now, now);
        
        // When
        Teacher entity = teacherMapper.toEntity(dto);
        
        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    void testToDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Teacher entity = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        // When
        TeacherDto dto = teacherMapper.toDto(entity);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        TeacherDto dto1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto dto2 = new TeacherDto(2L, "Smith", "Jane", now, now);
        List<TeacherDto> dtoList = List.of(dto1, dto2);
        
        // When
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);
        
        // Then
        assertThat(entityList).hasSize(2);
        assertThat(entityList.get(0).getId()).isEqualTo(1L);
        assertThat(entityList.get(0).getLastName()).isEqualTo("Doe");
        assertThat(entityList.get(1).getId()).isEqualTo(2L);
        assertThat(entityList.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void testToDtoList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Teacher entity1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();
        Teacher entity2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(now)
                .updatedAt(now)
                .build();
        List<Teacher> entityList = List.of(entity1, entity2);
        
        // When
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);
        
        // Then
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(1L);
        assertThat(dtoList.get(0).getLastName()).isEqualTo("Doe");
        assertThat(dtoList.get(1).getId()).isEqualTo(2L);
        assertThat(dtoList.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void testToEntity_withNullDto() {
        // When
        Teacher entity = teacherMapper.toEntity((TeacherDto) null);
        
        // Then
        assertThat(entity).isNull();
    }

    @Test
    void testToDto_withNullEntity() {
        // When
        TeacherDto dto = teacherMapper.toDto((Teacher) null);
        
        // Then
        assertThat(dto).isNull();
    }

    @Test
    void testToEntityList_withNullList() {
        // When
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);
        
        // Then
        assertThat(entityList).isNull();
    }

    @Test
    void testToDtoList_withNullList() {
        // When
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);
        
        // Then
        assertThat(dtoList).isNull();
    }
}
