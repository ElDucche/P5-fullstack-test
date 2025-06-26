package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(1L);
    }

    @Test
    void testCreate() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session created = sessionService.create(new Session());
        assertThat(created).isEqualTo(session);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void testDelete() {
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        List<Session> sessions = Collections.singletonList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> found = sessionService.findAll();
        assertThat(found).isEqualTo(sessions);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Session found = sessionService.getById(1L);
        assertThat(found).isEqualTo(session);
        verify(sessionRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetById_NotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        Session found = sessionService.getById(1L);
        assertThat(found).isNull();
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session updated = sessionService.update(1L, new Session());
        assertThat(updated.getId()).isEqualTo(1L);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void testParticipate() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        assertThat(session.getUsers()).contains(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }
    
    @Test
    void testParticipate_UserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testParticipate_AlreadyParticipating() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testNoLongerParticipate() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertThat(session.getUsers()).doesNotContain(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testNoLongerParticipate_NotParticipating() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }
}