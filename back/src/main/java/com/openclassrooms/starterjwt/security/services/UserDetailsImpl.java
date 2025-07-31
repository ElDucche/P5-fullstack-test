package com.openclassrooms.starterjwt.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  private String firstName;

  private String lastName;

  private Boolean admin;

  @JsonIgnore
  private String password;  
  
  public Collection<? extends GrantedAuthority> getAuthorities() {        
        return new HashSet<>();
    }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  } 
  
  @Override
  public int hashCode() {
        return Objects.hash(id);
    }
  
  // Ajout des méthodes attendues par le code existant et les tests
  public Long getId() {
      return id;
  }
  public String getFirstName() {
      return firstName;
  }
  public String getLastName() {
      return lastName;
  }
  public Boolean isAdmin() {
      return admin;
  }
  @Override
  public String getPassword() {
        return password;
    }
  @Override
  public String getUsername() {
      return username;
  }
  
  // Builder manuel pour UserDetailsImpl
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private Boolean admin;
        private String password;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder admin(Boolean admin) { this.admin = admin; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public UserDetailsImpl build() {
            return new UserDetailsImpl(id, username, firstName, lastName, admin, password);
        }
    }
    // Constructeur complet pour le builder manuel
    public UserDetailsImpl(Long id, String username, String firstName, String lastName, Boolean admin, String password) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
        this.password = password;
    }
}
