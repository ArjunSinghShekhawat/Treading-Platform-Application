package com.treadingPlatformApplication.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.treadingPlatformApplication.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    @Email(message = "please provide valid email")
    private String email;

    @Column(name = "password",length = 20)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "user_role")
    private UserRole userRole = UserRole.ROLE_CUSTOMER;

    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
}
