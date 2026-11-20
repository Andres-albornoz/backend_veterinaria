package com.example.backend_veterinaria.controller;

import com.example.backend_veterinaria.dto.UsuarioResponse;
import com.example.backend_veterinaria.dto.UsuarioUpdateDTO;
import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "usuarios y autenticación", description = "Registro, login y administración de usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/auth/register", "/auth/resgister"})
    @Operation(
            summary = "registra a un usuario",
            description = "registra a un usuario guardando sus datos en la base de datos"
    )
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "inicia sesion para usuario",
            description = "busca e inicia la sesión de un usuario"
    )
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Map<String, String> response = userService.login(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "listar usuarios", description = "obtiene todos los usuarios registrados")
    public List<UsuarioResponse> listar() {
        return userService.listar();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "buscar un usuario",
            description = "busca un usuario específico por su id"
    )
    public UsuarioResponse buscar(@PathVariable Integer id) {
        return userService.buscarPorId(id);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "actualizar un usuario",
            description = "actualizar los datos de un usuario de la base de datos mediante su id"
    )
    public UsuarioResponse actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return userService.actualizar(id, dto);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "eliminar un usuario",
            description = "elimina un usuario de la base de datos mediante su id"
    )
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        userService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
