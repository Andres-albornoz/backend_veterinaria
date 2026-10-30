package com.example.backend_veterinaria.controller;

import com.example.backend_veterinaria.dto.ServiciosDTO;
import com.example.backend_veterinaria.model.Servicios;
import com.example.backend_veterinaria.repository.ServiciosRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiciosControler {

    private final ServiciosRepository repo;

    public ServiciosControler(ServiciosRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Servicios> findAll() {
        return repo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicios> crear(@Valid @RequestBody ServiciosDTO DTO) {
        Servicios p = repo.save(new Servicios(DTO.categoria(), DTO.nombre(), DTO.especie(), DTO.duracion(), DTO.precio(), DTO.observaciones()));
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicios> actualizar(@PathVariable Long id, @Valid @RequestBody ServiciosDTO req) {
        return repo.findById(id)
                .map(s -> {
                    s.setCategoria(req.categoria());
                    s.setNombre(req.nombre());
                    s.setEspecie(req.especie());
                    s.setDuracion(req.duracion());
                    s.setPrecio(req.precio());
                    s.setObservaciones(req.observaciones());
                    return ResponseEntity.ok(repo.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
