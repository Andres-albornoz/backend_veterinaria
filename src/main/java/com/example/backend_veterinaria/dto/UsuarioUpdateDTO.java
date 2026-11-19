package com.example.backend_veterinaria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank String rol
) {
}
