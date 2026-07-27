package com.jumpstart.loadshedhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String newPassword;
}
