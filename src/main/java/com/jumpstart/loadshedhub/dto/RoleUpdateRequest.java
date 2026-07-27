package com.jumpstart.loadshedhub.dto;

import com.jumpstart.loadshedhub.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdateRequest {
    @NotNull
    private Role role;
}
