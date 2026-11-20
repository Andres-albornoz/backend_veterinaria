package com.example.backend_veterinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ServiciosDTO (
        @NotBlank @Size(max = 20) String categoria,
        @NotBlank @Size(max = 40) String nombre,
        @NotBlank @Size(max = 20) String especie,
        @NotNull @PositiveOrZero Integer duracion,
        @NotNull @PositiveOrZero Integer precio,
        @NotBlank @Size(max = 100) String observaciones
        ) {
}