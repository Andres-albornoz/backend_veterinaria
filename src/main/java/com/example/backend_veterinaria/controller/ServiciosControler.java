package com.example.backend_veterinaria.controller;

import com.example.backend_veterinaria.dto.ServiciosDTO;
import com.example.backend_veterinaria.model.Servicios;
import com.example.backend_veterinaria.service.ServiciosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/service", "/api/servicios"})
public class ServiciosControler {

    private final ServiciosService service;

    public ServiciosControler(ServiciosService service) {
        this.service = service;
    }

    @GetMapping
    public List<Servicios> findAll(@RequestParam(required = false) String categoria) {
        return categoria == null ? service.listar() : service.listarPorCategoria(categoria);
    }

    @GetMapping("/{id}")
    public Servicios findById(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicios> crear(@Valid @RequestBody ServiciosDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicios> actualizar(@PathVariable Long id, @Valid @RequestBody ServiciosDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
