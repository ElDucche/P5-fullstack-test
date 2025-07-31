package com.openclassrooms.starterjwt.config;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration de test pour fournir des mocks de tous les mappers
 * Cette configuration est utilisée uniquement pendant les tests d'intégration
 * pour résoudre le problème de beans manquants.
 * 
 * Les beans sont marqués @Primary pour avoir la priorité sur les implémentations
 * générées par MapStruct en cas de conflit.
 */
@TestConfiguration
@Profile("test")
public class TestMappersConfig {

    @Bean
    @Primary
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    @Primary
    public SessionMapper sessionMapper() {
        return new SessionMapper() {
            @Override
            public Session toEntity(SessionDto sessionDto) {
                if (sessionDto == null) {
                    return null;
                }
                Session session = new Session();
                session.setId(sessionDto.getId());
                session.setName(sessionDto.getName());
                session.setDate(sessionDto.getDate());
                session.setDescription(sessionDto.getDescription());
                session.setCreatedAt(sessionDto.getCreatedAt());
                session.setUpdatedAt(sessionDto.getUpdatedAt());
                return session;
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
                if (session.getTeacher() != null) {
                    dto.setTeacher_id(session.getTeacher().getId());
                }
                if (session.getUsers() != null) {
                    List<Long> userIds = new ArrayList<>();
                    session.getUsers().forEach(user -> userIds.add(user.getId()));
                    dto.setUsers(userIds);
                } else {
                    dto.setUsers(new ArrayList<>());
                }
                return dto;
            }

            @Override
            public List<Session> toEntity(List<SessionDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                List<Session> list = new ArrayList<>(dtoList.size());
                for (SessionDto sessionDto : dtoList) {
                    list.add(toEntity(sessionDto));
                }
                return list;
            }

            @Override
            public List<SessionDto> toDto(List<Session> entityList) {
                if (entityList == null) {
                    return null;
                }
                List<SessionDto> list = new ArrayList<>(entityList.size());
                for (Session session : entityList) {
                    list.add(toDto(session));
                }
                return list;
            }
        };
    }

    @Bean
    @Primary
    public TeacherMapper teacherMapper() {
        return new TeacherMapper() {
            @Override
            public Teacher toEntity(TeacherDto dto) {
                if (dto == null) {
                    return null;
                }
                Teacher teacher = new Teacher();
                teacher.setId(dto.getId());
                teacher.setFirstName(dto.getFirstName());
                teacher.setLastName(dto.getLastName());
                teacher.setCreatedAt(dto.getCreatedAt());
                teacher.setUpdatedAt(dto.getUpdatedAt());
                return teacher;
            }

            @Override
            public TeacherDto toDto(Teacher entity) {
                if (entity == null) {
                    return null;
                }
                TeacherDto dto = new TeacherDto();
                dto.setId(entity.getId());
                dto.setFirstName(entity.getFirstName());
                dto.setLastName(entity.getLastName());
                dto.setCreatedAt(entity.getCreatedAt());
                dto.setUpdatedAt(entity.getUpdatedAt());
                return dto;
            }

            @Override
            public List<Teacher> toEntity(List<TeacherDto> dtoList) {
                if (dtoList == null) {
                    return null;
                }
                List<Teacher> list = new ArrayList<>(dtoList.size());
                for (TeacherDto dto : dtoList) {
                    list.add(toEntity(dto));
                }
                return list;
            }

            @Override
            public List<TeacherDto> toDto(List<Teacher> entityList) {
                if (entityList == null) {
                    return null;
                }
                List<TeacherDto> list = new ArrayList<>(entityList.size());
                for (Teacher entity : entityList) {
                    list.add(toDto(entity));
                }
                return list;
            }
        };
    }

    @Bean
    @Primary
    public UserMapper userMapper() {
        return new UserMapper() {
            @Override
            public User toEntity(UserDto dto) {
                if (dto == null) {
                    return null;
                }
                User user = new User();
                user.setId(dto.getId());
                user.setEmail(dto.getEmail());
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setPassword(dto.getPassword());
                user.setAdmin(dto.isAdmin());
                user.setCreatedAt(dto.getCreatedAt());
                user.setUpdatedAt(dto.getUpdatedAt());
                return user;
            }

            @Override
            public UserDto toDto(User entity) {
                if (entity == null) {
                    return null;
                }
                UserDto dto = new UserDto();
                dto.setId(entity.getId());
                dto.setEmail(entity.getEmail());
                dto.setFirstName(entity.getFirstName());
                dto.setLastName(entity.getLastName());
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
                List<User> list = new ArrayList<>(dtoList.size());
                for (UserDto dto : dtoList) {
                    list.add(toEntity(dto));
                }
                return list;
            }

            @Override
            public List<UserDto> toDto(List<User> entityList) {
                if (entityList == null) {
                    return null;
                }
                List<UserDto> list = new ArrayList<>(entityList.size());
                for (User entity : entityList) {
                    list.add(toDto(entity));
                }
                return list;
            }
        };
    }
}
