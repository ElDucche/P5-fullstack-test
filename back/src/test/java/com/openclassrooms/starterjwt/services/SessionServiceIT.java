package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class SessionServiceIT {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

        user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);
    }

    @Test
    void testCreateAndGetById() {
        Session createdSession = sessionService.getById(session.getId());
        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Yoga Session");
    }

    @Test
    void testFindAll() {
        List<Session> sessions = sessionService.findAll();
        assertThat(sessions).isNotNull().hasSize(1);
    }

    @Test
    void testUpdate() {
        Long sessionId = session.getId();
        Session sessionToUpdate = new Session();
        sessionToUpdate.setName("Advanced Yoga Session");
        sessionToUpdate.setDescription("An advanced yoga session");
        sessionToUpdate.setDate(new Date());
        sessionToUpdate.setTeacher(teacher);

        Session updatedSession = sessionService.update(sessionId, sessionToUpdate);

        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getName()).isEqualTo("Advanced Yoga Session");
    }

    @Test
    void testDelete() {
        Long sessionId = session.getId();
        sessionService.delete(sessionId);
        assertThat(sessionRepository.findById(sessionId)).isEmpty();
    }

    @Test
    void testParticipate() {
        Long sessionId = session.getId();
        Long userId = user.getId();

        sessionService.participate(sessionId, userId);

        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getUsers()).contains(user);
    }

    @Test
    void testParticipate_alreadyParticipating() {
        Long sessionId = session.getId();
        Long userId = user.getId();
        sessionService.participate(sessionId, userId);

        assertThatThrownBy(() -> sessionService.participate(sessionId, userId))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void testNoLongerParticipate() {
        Long sessionId = session.getId();
        Long userId = user.getId();
        sessionService.participate(sessionId, userId);

        sessionService.noLongerParticipate(sessionId, userId);

        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getUsers()).doesNotContain(user);
    }

    @Test
    void testNoLongerParticipate_notParticipating() {
        Long sessionId = session.getId();
        Long userId = user.getId();

        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, userId))
                .isInstanceOf(BadRequestException.class);
    }
}
