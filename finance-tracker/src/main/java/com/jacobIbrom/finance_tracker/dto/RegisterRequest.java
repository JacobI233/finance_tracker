package com.jacobIbrom.finance_tracker.dto;

import org.jspecify.annotations.Nullable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name required")
    private String name;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email required")
    private String email;


    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;


}

