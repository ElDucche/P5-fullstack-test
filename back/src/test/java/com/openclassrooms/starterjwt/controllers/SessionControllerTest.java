package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
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

public class SessionControllerTest {
    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;
    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new Session();
        session.setId(1L);
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
    }

    @Test
    void findById_shouldReturnSessionDto_whenFound() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        ResponseEntity<?> response = sessionController.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(sessionDto);
    }

    @Test
    void findById_shouldReturnNotFound_whenSessionIsNull() {
        when(sessionService.getById(2L)).thenReturn(null);
        ResponseEntity<?> response = sessionController.findById("2");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.findById("notANumber");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void findAll_shouldReturnListOfSessionDtos() {
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> dtos = Arrays.asList(sessionDto);
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(dtos);
        ResponseEntity<?> response = sessionController.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dtos);
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(sessionService.findAll()).thenReturn(Collections.emptyList());
        when(sessionMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionController.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(Collections.emptyList());
    }

    @Test
    void create_shouldReturnSessionDto() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        ResponseEntity<?> response = sessionController.create(sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(sessionDto);
    }

    @Test
    void update_shouldReturnSessionDto_whenIdIsValid() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        ResponseEntity<?> response = sessionController.update("1", sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(sessionDto);
    }

    @Test
    void update_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.update("notANumber", sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void save_shouldReturnOk_whenSessionExists() {
        when(sessionService.getById(1L)).thenReturn(session);
        ResponseEntity<?> response = sessionController.save("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(sessionService).delete(1L);
    }

    @Test
    void save_shouldReturnNotFound_whenSessionIsNull() {
        when(sessionService.getById(2L)).thenReturn(null);
        ResponseEntity<?> response = sessionController.save("2");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void save_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.save("notANumber");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void participate_shouldReturnOk_whenIdsAreValid() {
        ResponseEntity<?> response = sessionController.participate("1", "2");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.participate("notANumber", "2");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void noLongerParticipate_shouldReturnOk_whenIdsAreValid() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_shouldReturnBadRequest_whenIdIsInvalid() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("notANumber", "2");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}
