package com.treadingPlatformApplication.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "two_factor_otp")
public class TwoFactorOtp {
    @Id
    private String id;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    private String otp;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;
}
