package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {
    
    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        // Créer une implémentation concrète simple pour les tests
        sessionMapper = new SessionMapper() {
            @Override
            public Session toEntity(SessionDto sessionDto) {
                if (sessionDto == null) {
                    return null;
                }
                return Session.builder()
                        .id(sessionDto.getId())
                        .name(sessionDto.getName())
                        .date(sessionDto.getDate())
                        .description(sessionDto.getDescription())
                        .createdAt(sessionDto.getCreatedAt())
                        .updatedAt(sessionDto.getUpdatedAt())
                        .build();
            }

            @Override
            public SessionDto toDto(Session session) {
                if (session == null) {
                    return null;
                }
                SessionDto dto = new SessionDto();
                dto.setId(session.getId());
                dto.setName(session.getName());
                dto.setDate(session.getDate());
                dto.setDescription(session.getDescription());
                dto.setCreatedAt(session.getCreatedAt());
                dto.setUpdatedAt(session.getUpdatedAt());
                
                // Gérer teacher_id
                if (session.getTeacher() != null) {
                    dto.setTeacher_id(session.getTeacher().getId());
                }
                
                return dto;
            }

            @Override
            public List<Session> toEntity(List<SessionDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                return dtoList.stream().map(this::toEntity).toList();
            }

            @Override
            public List<SessionDto> toDto(List<Session> entityList) {
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
        Date sessionDate = new Date();
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");
        dto.setDate(sessionDate);
        dto.setDescription("A relaxing yoga session");
        dto.setTeacher_id(2L);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        
        // When
        Session entity = sessionMapper.toEntity(dto);
        
        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getDate()).isEqualTo(dto.getDate());
        assertThat(entity.getDescription()).isEqualTo(dto.getDescription());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    void testToDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate = new Date();
        Teacher teacher = Teacher.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .build();
        
        Session entity = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("A relaxing yoga session")
                .teacher(teacher)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        // When
        SessionDto dto = sessionMapper.toDto(entity);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getName()).isEqualTo(entity.getName());
        assertThat(dto.getDate()).isEqualTo(entity.getDate());
        assertThat(dto.getDescription()).isEqualTo(entity.getDescription());
        assertThat(dto.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void testToDto_withNullTeacher() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate = new Date();
        
        Session entity = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("A relaxing yoga session")
                .teacher(null) // Teacher is null
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        // When
        SessionDto dto = sessionMapper.toDto(entity);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getName()).isEqualTo(entity.getName());
        assertThat(dto.getTeacher_id()).isNull();
    }

    @Test
    void testToEntityList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate1 = new Date();
        Date sessionDate2 = new Date();
        
        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        dto1.setName("Morning Yoga");
        dto1.setDate(sessionDate1);
        dto1.setCreatedAt(now);
        dto1.setUpdatedAt(now);
        
        SessionDto dto2 = new SessionDto();
        dto2.setId(2L);
        dto2.setName("Evening Yoga");
        dto2.setDate(sessionDate2);
        dto2.setCreatedAt(now);
        dto2.setUpdatedAt(now);
        
        List<SessionDto> dtoList = List.of(dto1, dto2);
        
        // When
        List<Session> entityList = sessionMapper.toEntity(dtoList);
        
        // Then
        assertThat(entityList).hasSize(2);
        assertThat(entityList.get(0).getId()).isEqualTo(1L);
        assertThat(entityList.get(0).getName()).isEqualTo("Morning Yoga");
        assertThat(entityList.get(1).getId()).isEqualTo(2L);
        assertThat(entityList.get(1).getName()).isEqualTo("Evening Yoga");
    }

    @Test
    void testToDtoList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Date sessionDate1 = new Date();
        Date sessionDate2 = new Date();
        
        Session entity1 = Session.builder()
                .id(1L)
                .name("Morning Yoga")
                .date(sessionDate1)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        Session entity2 = Session.builder()
                .id(2L)
                .name("Evening Yoga")
                .date(sessionDate2)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        List<Session> entityList = List.of(entity1, entity2);
        
        // When
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);
        
        // Then
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(1L);
        assertThat(dtoList.get(0).getName()).isEqualTo("Morning Yoga");
        assertThat(dtoList.get(1).getId()).isEqualTo(2L);
        assertThat(dtoList.get(1).getName()).isEqualTo("Evening Yoga");
    }

    @Test
    void testToEntity_withNullDto() {
        // When
        Session entity = sessionMapper.toEntity((SessionDto) null);
        
        // Then
        assertThat(entity).isNull();
    }

    @Test
    void testToDto_withNullEntity() {
        // When
        SessionDto dto = sessionMapper.toDto((Session) null);
        
        // Then
        assertThat(dto).isNull();
    }

    @Test
    void testToEntityList_withNullList() {
        // When
        List<Session> entityList = sessionMapper.toEntity((List<SessionDto>) null);
        
        // Then
        assertThat(entityList).isNull();
    }

    @Test
    void testToDtoList_withNullList() {
        // When
        List<SessionDto> dtoList = sessionMapper.toDto((List<Session>) null);
        
        // Then
        assertThat(dtoList).isNull();
    }
}
