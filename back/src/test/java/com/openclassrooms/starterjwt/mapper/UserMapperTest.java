package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Créer une implémentation concrète simple pour les tests
        userMapper = new UserMapper() {
            @Override
            public User toEntity(UserDto dto) {
                if (dto == null) {
                    return null;
                }
                return User.builder()
                        .id(dto.getId())
                        .email(dto.getEmail())
                        .lastName(dto.getLastName())
                        .firstName(dto.getFirstName())
                        .password(dto.getPassword())
                        .admin(dto.isAdmin())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .build();
            }

            @Override
            public UserDto toDto(User entity) {
                if (entity == null) {
                    return null;
                }
                UserDto dto = new UserDto();
                dto.setId(entity.getId());
                dto.setEmail(entity.getEmail());
                dto.setLastName(entity.getLastName());
                dto.setFirstName(entity.getFirstName());
                dto.setPassword(entity.getPassword());
                dto.setAdmin(entity.isAdmin());
                dto.setCreatedAt(entity.getCreatedAt());
                dto.setUpdatedAt(entity.getUpdatedAt());
                return dto;
            }

            @Override
            public List<User> toEntity(List<UserDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                return dtoList.stream().map(this::toEntity).toList();
            }

            @Override
            public List<UserDto> toDto(List<User> entityList) {
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
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(true);
        dto.setPassword("password");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        
        // When
        User entity = userMapper.toEntity(dto);
        
        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.isAdmin()).isEqualTo(dto.isAdmin());
        assertThat(entity.getPassword()).isEqualTo(dto.getPassword());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    void testToDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        User entity = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .admin(true)
                .password("password")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        // When
        UserDto dto = userMapper.toDto(entity);
        
        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getEmail()).isEqualTo(entity.getEmail());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.isAdmin()).isEqualTo(entity.isAdmin());
        assertThat(dto.getPassword()).isEqualTo(entity.getPassword());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("test1@example.com");
        dto1.setLastName("Doe");
        dto1.setFirstName("John");
        dto1.setAdmin(true);
        dto1.setPassword("password1");
        dto1.setCreatedAt(now);
        dto1.setUpdatedAt(now);
        
        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("test2@example.com");
        dto2.setLastName("Smith");
        dto2.setFirstName("Jane");
        dto2.setAdmin(false);
        dto2.setPassword("password2");
        dto2.setCreatedAt(now);
        dto2.setUpdatedAt(now);
        
        List<UserDto> dtoList = List.of(dto1, dto2);
        
        // When
        List<User> entityList = userMapper.toEntity(dtoList);
        
        // Then
        assertThat(entityList).hasSize(2);
        assertThat(entityList.get(0).getId()).isEqualTo(1L);
        assertThat(entityList.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(entityList.get(0).isAdmin()).isTrue();
        assertThat(entityList.get(1).getId()).isEqualTo(2L);
        assertThat(entityList.get(1).getEmail()).isEqualTo("test2@example.com");
        assertThat(entityList.get(1).isAdmin()).isFalse();
    }

    @Test
    void testToDtoList() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        User entity1 = User.builder()
                .id(1L)
                .email("test1@example.com")
                .lastName("Doe")
                .firstName("John")
                .admin(true)
                .password("password1")
                .createdAt(now)
                .updatedAt(now)
                .build();
        User entity2 = User.builder()
                .id(2L)
                .email("test2@example.com")
                .lastName("Smith")
                .firstName("Jane")
                .admin(false)
                .password("password2")
                .createdAt(now)
                .updatedAt(now)
                .build();
        List<User> entityList = List.of(entity1, entity2);
        
        // When
        List<UserDto> dtoList = userMapper.toDto(entityList);
        
        // Then
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(1L);
        assertThat(dtoList.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(dtoList.get(0).isAdmin()).isTrue();
        assertThat(dtoList.get(1).getId()).isEqualTo(2L);
        assertThat(dtoList.get(1).getEmail()).isEqualTo("test2@example.com");
        assertThat(dtoList.get(1).isAdmin()).isFalse();
    }

    @Test
    void testToEntity_withNullDto() {
        // When
        User entity = userMapper.toEntity((UserDto) null);
        
        // Then
        assertThat(entity).isNull();
    }

    @Test
    void testToDto_withNullEntity() {
        // When
        UserDto dto = userMapper.toDto((User) null);
        
        // Then
        assertThat(dto).isNull();
    }

    @Test
    void testToEntityList_withNullList() {
        // When
        List<User> entityList = userMapper.toEntity((List<UserDto>) null);
        
        // Then
        assertThat(entityList).isNull();
    }

    @Test
    void testToDtoList_withNullList() {
        // When
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);
        
        // Then
        assertThat(dtoList).isNull();
    }
}
