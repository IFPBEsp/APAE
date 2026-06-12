package br.org.apae.api.auth.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import br.org.apae.api.address.domain.model.Address;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(unique = true)
  private String cpf;

  @Column(name = "senha")
  private String password;

  @Column(name = "primeiro_acesso", nullable = false)
  private boolean firstAccess = false;

  @Column(name = "nome_completo")
  private String fullName;

  @Column(name = "contato")
  private String phoneNumber;

  @Column(name = "rg", unique = true)
  private String identityDocument;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "endereco_id", referencedColumnName = "id", unique = true)
  private Address address;

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

  public User(String email, String password, String cpf, String fullName, UserRole role,
      String phoneNumber, String identityDocument) {
    this(email, password, cpf, fullName, role);
    this.phoneNumber = phoneNumber;
    this.identityDocument = identityDocument;
  }

  public User(String email, String password, String cpf, String fullName, UserRole role,
      String phoneNumber, String identityDocument, Address address, boolean firstAccess) {
    this(email, password, cpf, fullName, role, phoneNumber, identityDocument);
    this.address = address;
    this.firstAccess = firstAccess;
  }

  public static User createAuthenticatedUser(String email, String password, String cpf, String fullName,
      UserRole role) {
    return new User(email, password, cpf, fullName, role);
  }

  public static User createProfessionalUser(String email, String cpf, String fullName, String phoneNumber,
      String identityDocument, Address address) {
    return new User(email, null, cpf, fullName, UserRole.ATENDIMENTO, phoneNumber, identityDocument, address, true);
  }

  public UUID getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public boolean hasConfiguredPassword() {
    return password != null && !password.isBlank();
  }

  public boolean isFirstAccess() {
    return firstAccess;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getIdentityDocument() {
    return identityDocument;
  }

  public Address getAddress() {
    return address;
  }

  public void updateProfile(String email, String fullName, String phoneNumber, String identityDocument) {
    this.email = email;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.identityDocument = identityDocument;
  }

  public void updateCpf(String cpf) {
    this.cpf = cpf;
  }

  public void updateAddress(Address address) {
    this.address = address;
  }

  public void updatePassword(String password) {
    this.password = password;
    this.firstAccess = false;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }
}
