package com.example.backend_veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoDTO(
        @NotBlank String nombre,
        String descripcion,
        @NotNull @PositiveOrZero Integer precio,
        @PositiveOrZero Integer stock,
        Boolean activo
) {
}
