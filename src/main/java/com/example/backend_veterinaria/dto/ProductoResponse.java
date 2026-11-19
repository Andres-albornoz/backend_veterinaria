package com.example.backend_veterinaria.dto;

import com.example.backend_veterinaria.model.Producto;

public record ProductoResponse(
        Integer id,
        String nombre,
        String descripcion,
        int precio,
        Integer stock,
        Boolean activo
) {
    public static ProductoResponse de(Producto producto) {
        return new ProductoResponse(
                producto.getId(), producto.getNombre(), producto.getDescripcion(),
                producto.getPrecio(), producto.getStock(), producto.getActivo());
    }
}
