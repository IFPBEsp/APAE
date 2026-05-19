package br.org.apae.api.auth.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String cpf;

  @Column(name = "senha", nullable = false)
  private String password;

  @Column(name = "nome_completo")
  private String fullName;

  @Enumerated(EnumType.STRING)
  @Column(name = "cargo", nullable = false)
  private UserRole role;

  protected User() {
  }

  public User(String email, String password, String cpf, String fullName) {
    this.email = email;
    this.password = password;
    this.cpf = cpf;
    this.fullName = fullName;
  }

  public User(String email, String password, String cpf, String fullName, UserRole role) {
    this.email = email;
    this.password = password;
    this.cpf = cpf;
    this.fullName = fullName;
    this.role = role;
  }

  public UUID getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  public UserRole getRole() {
    return role;
  }

  public String getCpf() {
    return cpf;
  }

  public String getFullName() {
    return fullName;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role == UserRole.ADMIN
        ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"))
        : List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }
}
