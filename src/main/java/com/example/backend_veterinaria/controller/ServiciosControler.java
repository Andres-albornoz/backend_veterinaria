package com.example.backend_veterinaria.controller;

import com.example.backend_veterinaria.dto.ServiciosDTO;
import com.example.backend_veterinaria.model.Servicios;
import com.example.backend_veterinaria.service.ServiciosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/service", "/api/servicios"})
@Tag(name = "servicios", description = "Administración de los servicios de la veterinaria")
public class ServiciosControler {

    private final ServiciosService service;

    public ServiciosControler(ServiciosService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "Listar servicios",
            description = "Obtiene todos los servicios registrados"
    )
    public List<Servicios> findAll(@RequestParam(required = false) String categoria) {
        return categoria == null ? service.listar() : service.listarPorCategoria(categoria);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "buscar servicio",
            description = "busca un servicio por su id"
    )
    public Servicios findById(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "crear servicio",
            description = "permite crear un nuevo servicio"
    )
    public ResponseEntity<Servicios> crear(@Valid @RequestBody ServiciosDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "actualizar un servicio",
            description = "actualiza los datos de un servicio"
    )
    public ResponseEntity<Servicios> actualizar(@PathVariable Long id, @Valid @RequestBody ServiciosDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "eliminar un servicio",
            description = "elimina un servicio por su id"
    )
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
