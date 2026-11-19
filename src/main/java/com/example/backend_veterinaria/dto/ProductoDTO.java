package com.example.backend_veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoDTO(
        @NotBlank String nombre,
        String descripcion,
        @PositiveOrZero int precio,
        @PositiveOrZero Integer stock,
        Boolean activo
) {
}
