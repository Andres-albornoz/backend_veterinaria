package com.example.backend_veterinaria.dto;

import com.example.backend_veterinaria.model.User;

public record UsuarioResponse(
        Integer id,
        String nombre,
        String email,
        String rol
) {
    public static UsuarioResponse de(User user) {
        return new UsuarioResponse(user.getId(), user.getNombre(), user.getEmail(), user.getRol());
    }
}
