package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TeacherServiceIT {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);
    }

    @Test
    void testFindById() {
        Teacher foundTeacher = teacherService.findById(teacher.getId());

        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getId()).isEqualTo(teacher.getId());
        assertThat(foundTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
    }

    @Test
    void testFindById_NotFound() {
        Teacher foundTeacher = teacherService.findById(999L);
        assertThat(foundTeacher).isNull();
    }

    @Test
    void testFindAll() {
        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
        teacherRepository.save(teacher2);

        List<Teacher> teachers = teacherService.findAll();

        assertThat(teachers).isNotNull().hasSize(2);
    }
}
