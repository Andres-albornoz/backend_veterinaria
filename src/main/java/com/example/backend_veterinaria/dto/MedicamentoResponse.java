package com.example.backend_veterinaria.dto;

import com.example.backend_veterinaria.model.MedicamentosVacunas;

public record MedicamentoResponse(
        String codigo,
        String categoria,
        String nombre,
        String principioActivo,
        String presentacion,
        String especie,
        int stock,
        int precio
) {
    public static MedicamentoResponse de(MedicamentosVacunas medicamento) {
        return new MedicamentoResponse(
                medicamento.getCodigo(), medicamento.getCategoria(), medicamento.getNombre(),
                medicamento.getPrincipioActivo(), medicamento.getPresentacion(), medicamento.getEspecie(),
                medicamento.getStock(), medicamento.getPrecio());
    }
}
