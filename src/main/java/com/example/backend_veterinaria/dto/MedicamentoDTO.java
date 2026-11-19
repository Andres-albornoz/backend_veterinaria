package com.example.backend_veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record MedicamentoDTO(
        @NotBlank String codigo,
        String categoria,
        @NotBlank String nombre,
        String principioActivo,
        String presentacion,
        String especie,
        @PositiveOrZero int stock,
        @PositiveOrZero int precio
) {
}
