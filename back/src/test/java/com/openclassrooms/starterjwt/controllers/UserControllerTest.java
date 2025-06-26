package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("john.doe@test.com");
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@test.com");
        // Mock le contexte de sécurité pour les tests DELETE
        SecurityContextHolder.clearContext();
    }

    @Test
    void findById_shouldReturnUserDto_whenFound() {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        ResponseEntity<?> response = userController.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    void findById_shouldReturnNotFound_whenUserIsNull() {
        when(userService.findById(2L)).thenReturn(null);
        ResponseEntity<?> response = userController.findById("2");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = userController.findById("notANumber");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void delete_shouldReturnOk_whenUserIsOwner() {
        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("john.doe@test.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        ResponseEntity<?> response = userController.save("1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).delete(1L);
    }

    @Test
    void delete_shouldReturnNotFound_whenUserIsNull() {
        when(userService.findById(2L)).thenReturn(null);
        ResponseEntity<?> response = userController.save("2");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = userController.save("notANumber");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void delete_shouldReturnUnauthorized_whenUserIsNotOwner() {
        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("other@test.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        ResponseEntity<?> response = userController.save("1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(userService, never()).delete(anyLong());
    }
}
