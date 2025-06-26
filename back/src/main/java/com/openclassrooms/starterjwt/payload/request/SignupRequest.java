package com.openclassrooms.starterjwt.payload.request;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 20)
  private String firstName;

  @NotBlank
  @Size(min = 3, max = 20)
  private String lastName;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  // Lombok génère déjà les getters/setters, mais on ajoute explicitement les méthodes attendues par le code existant :
  public String getEmail() {
      return email;
  }
  public String getFirstName() {
      return firstName;
  }
  public String getLastName() {
      return lastName;
  }
  public String getPassword() {
      return password;
  }
}
