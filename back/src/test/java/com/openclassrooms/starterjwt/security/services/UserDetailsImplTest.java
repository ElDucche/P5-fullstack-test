package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setAdmin(false);
    }

    @Test
    void testBuild() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();

        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
        assertThat(userDetails.getFirstName()).isEqualTo("firstName");
        assertThat(userDetails.getLastName()).isEqualTo("lastName");
        assertThat(userDetails.getAdmin()).isFalse();
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }

    @Test
    void testGetters() {
        user.setAdmin(true);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();

        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
        assertThat(userDetails.getFirstName()).isEqualTo("firstName");
        assertThat(userDetails.getLastName()).isEqualTo("lastName");
        assertThat(userDetails.getAdmin()).isTrue();
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }

    @Test
    void testAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    void testAccountStatus() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void testEquals() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();

        User user2 = new User();
        user2.setId(1L);
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(user2.getId())
                .username(user2.getEmail())
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .admin(user2.isAdmin())
                .password(user2.getPassword())
                .build();

        User user3 = new User();
        user3.setId(2L);
        UserDetailsImpl userDetails3 = UserDetailsImpl.builder()
                .id(user3.getId())
                .username(user3.getEmail())
                .firstName(user3.getFirstName())
                .lastName(user3.getLastName())
                .admin(user3.isAdmin())
                .password(user3.getPassword())
                .build();

        assertThat(userDetails1)
                .isEqualTo(userDetails2) // same id
                .isNotEqualTo(userDetails3) // different id
                .isNotEqualTo(null)
                .isNotEqualTo(new Object())
                .isEqualTo(userDetails1);
    }

    @Test
    void testHashCode() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .password(user.getPassword())
                .build();

        User user2 = new User();
        user2.setId(1L);
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(user2.getId())
                .username(user2.getEmail())
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .admin(user2.isAdmin())
                .password(user2.getPassword())
                .build();

        User user3 = new User();
        user3.setId(2L);
        UserDetailsImpl userDetails3 = UserDetailsImpl.builder()
                .id(user3.getId())
                .username(user3.getEmail())
                .firstName(user3.getFirstName())
                .lastName(user3.getLastName())
                .admin(user3.isAdmin())
                .password(user3.getPassword())
                .build();

        assertThat(userDetails1).hasSameHashCodeAs(userDetails2);
        assertThat(userDetails1.hashCode()).isNotEqualTo(userDetails3.hashCode());
    }
}
