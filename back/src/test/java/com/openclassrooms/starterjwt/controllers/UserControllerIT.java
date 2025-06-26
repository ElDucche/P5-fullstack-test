package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void findById_shouldReturnUser_whenExists() throws Exception {
        User user = new User();
        user.setEmail("john.doe@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("pass");
        user.setAdmin(false);
        user = userRepository.save(user);
        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("john.doe@test.com"));
    }

    @Test
    void findById_shouldReturnNotFound_whenNotExists() throws Exception {
        mockMvc.perform(get("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com")
    void delete_shouldReturnOk_whenUserIsOwner() throws Exception {
        User user = new User();
        user.setEmail("john.doe@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("pass");
        user.setAdmin(false);
        user = userRepository.save(user);
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void delete_shouldReturnUnauthorized_whenUserIsNotOwner() throws Exception {
        User user = new User();
        user.setEmail("john.doe@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("pass");
        user.setAdmin(false);
        user = userRepository.save(user);
        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_shouldReturnNotFound_whenUserNotExists() throws Exception {
        mockMvc.perform(delete("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnBadRequest_whenIdInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}
