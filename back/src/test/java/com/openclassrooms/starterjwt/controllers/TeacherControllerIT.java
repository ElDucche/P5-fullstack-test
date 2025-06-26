package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
    }

    @Test
    void findById_shouldReturnTeacher_whenExists() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);
        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void findById_shouldReturnNotFound_whenNotExists() throws Exception {
        mockMvc.perform(get("/api/teacher/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnList() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}
