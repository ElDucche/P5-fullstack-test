package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
class UserMapperIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toEntity_shouldMapUserDtoToUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password123");
        userDto.setAdmin(true);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        // When
        User user = userMapper.toEntity(userDto);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isAdmin()).isEqualTo(userDto.isAdmin());
        assertThat(user.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(user.getUpdatedAt()).isEqualTo(userDto.getUpdatedAt());
    }

    @Test
    void toEntity_shouldReturnNullForNullInput() {
        // When
        User user = userMapper.toEntity((UserDto) null);

        // Then
        assertThat(user).isNull();
    }

    @Test
    void toDto_shouldMapUserToUserDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // When
        UserDto userDto = userMapper.toDto(user);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.isAdmin()).isEqualTo(user.isAdmin());
        assertThat(userDto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(userDto.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        // When
        UserDto userDto = userMapper.toDto((User) null);

        // Then
        assertThat(userDto).isNull();
    }

    @Test
    void toEntity_shouldMapListOfUserDtoToListOfUser() {
        // Given
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("test1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("test2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        // When
        List<User> users = userMapper.toEntity(userDtos);

        // Then
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId()).isEqualTo(1L);
        assertThat(users.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(users.get(0).getFirstName()).isEqualTo("John");
        assertThat(users.get(0).getLastName()).isEqualTo("Doe");
        assertThat(users.get(1).getId()).isEqualTo(2L);
        assertThat(users.get(1).getEmail()).isEqualTo("test2@example.com");
        assertThat(users.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(users.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void toDto_shouldMapListOfUserToListOfUserDto() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        List<User> users = Arrays.asList(user1, user2);

        // When
        List<UserDto> userDtos = userMapper.toDto(users);

        // Then
        assertThat(userDtos).hasSize(2);
        assertThat(userDtos.get(0).getId()).isEqualTo(1L);
        assertThat(userDtos.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(userDtos.get(0).getFirstName()).isEqualTo("John");
        assertThat(userDtos.get(0).getLastName()).isEqualTo("Doe");
        assertThat(userDtos.get(1).getId()).isEqualTo(2L);
        assertThat(userDtos.get(1).getEmail()).isEqualTo("test2@example.com");
        assertThat(userDtos.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(userDtos.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    void toEntity_shouldReturnNullForNullList() {
        // When
        List<User> users = userMapper.toEntity((List<UserDto>) null);

        // Then
        assertThat(users).isNull();
    }

    @Test
    void toDto_shouldReturnNullForNullList() {
        // When
        List<UserDto> userDtos = userMapper.toDto((List<User>) null);

        // Then
        assertThat(userDtos).isNull();
    }
}
