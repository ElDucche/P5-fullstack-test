package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("john.doe@test.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(true);
        dto.setPassword("secret");
        // createdAt et updatedAt peuvent rester null pour ce test
        User entity = userMapper.toEntity(dto);
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.isAdmin()).isEqualTo(dto.isAdmin());
        assertThat(entity.getPassword()).isEqualTo(dto.getPassword());
    }

    @Test
    void testToDto() {
        User entity = new User();
        entity.setId(1L);
        entity.setEmail("john.doe@test.com");
        entity.setLastName("Doe");
        entity.setFirstName("John");
        entity.setPassword("secret");
        entity.setAdmin(true);
        // createdAt et updatedAt peuvent rester null pour ce test
        UserDto dto = userMapper.toDto(entity);
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getEmail()).isEqualTo(entity.getEmail());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.isAdmin()).isEqualTo(entity.isAdmin());
        assertThat(dto.getPassword()).isEqualTo(entity.getPassword());
    }

    @Test
    void testToEntityList() {
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("test1@test.com");
        dto1.setFirstName("FirstName1");
        dto1.setLastName("LastName1");
        dto1.setPassword("pass1");
        dto1.setAdmin(true);

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("test2@test.com");
        dto2.setFirstName("FirstName2");
        dto2.setLastName("LastName2");
        dto2.setPassword("pass2");
        dto2.setAdmin(false);

        List<UserDto> dtoList = new ArrayList<>();
        dtoList.add(dto1);
        dtoList.add(dto2);

        List<User> entityList = userMapper.toEntity(dtoList);

        assertThat(entityList).isNotNull().hasSize(2);
        assertThat(entityList.get(0).getId()).isEqualTo(1L);
        assertThat(entityList.get(1).getId()).isEqualTo(2L);
        assertThat(entityList.get(0).getEmail()).isEqualTo("test1@test.com");
        assertThat(entityList.get(1).getEmail()).isEqualTo("test2@test.com");
    }

    @Test
    void testToDtoList() {
        User entity1 = new User();
        entity1.setId(1L);
        entity1.setEmail("test1@test.com");
        entity1.setFirstName("FirstName1");
        entity1.setLastName("LastName1");
        entity1.setPassword("pass1");
        entity1.setAdmin(true);

        User entity2 = new User();
        entity2.setId(2L);
        entity2.setEmail("test2@test.com");
        entity2.setFirstName("FirstName2");
        entity2.setLastName("LastName2");
        entity2.setPassword("pass2");
        entity2.setAdmin(false);

        List<User> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);

        List<UserDto> dtoList = userMapper.toDto(entityList);

        assertThat(dtoList).isNotNull().hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(1L);
        assertThat(dtoList.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void testToEntityList_withNullList() {
        List<User> entityList = userMapper.toEntity((List<UserDto>) null);
        assertThat(entityList).isNull();
    }

    @Test
    void testToDtoList_withNullList() {
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);
        assertThat(dtoList).isNull();
    }

    @Test
    void testToEntity_withNullDto() {
        User entity = userMapper.toEntity((UserDto) null);
        assertThat(entity).isNull();
    }

    @Test
    void testToDto_withNullEntity() {
        UserDto dto = userMapper.toDto((User) null);
        assertThat(dto).isNull();
    }
}
