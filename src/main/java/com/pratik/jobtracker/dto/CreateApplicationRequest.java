package com.pratik.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateApplicationRequest(

        @NotBlank(message = "Company is required")
        @Size(max = 100, message = "Company must be at most 100 characters")
        String company,

        @NotBlank(message = "Role is required")
        @Size(max = 100, message = "Role must be at most 100 characters")
        String role

) {
}