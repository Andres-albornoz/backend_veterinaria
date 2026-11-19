package com.example.backend_veterinaria.controller;

import com.example.backend_veterinaria.dto.MedicamentoDTO;
import com.example.backend_veterinaria.dto.MedicamentoResponse;
import com.example.backend_veterinaria.dto.ProductoDTO;
import com.example.backend_veterinaria.dto.ProductoResponse;
import com.example.backend_veterinaria.service.CatalogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CatalogoController {

    private final CatalogoService service;

    public CatalogoController(CatalogoService service) {
        this.service = service;
    }

    /* ---------- Productos ---------- */

    @GetMapping("/api/productos")
    public List<ProductoResponse> listarProductos(@RequestParam(required = false) String nombre) {
        return nombre == null
                ? service.listarProductos().stream().map(ProductoResponse::de).toList()
                : service.buscarProductosPorNombre(nombre).stream().map(ProductoResponse::de).toList();
    }

    @GetMapping("/api/productos/{id}")
    public ProductoResponse buscarProducto(@PathVariable int id) {
        return ProductoResponse.de(service.buscarProducto(id));
    }

    @PostMapping("/api/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoResponse.de(service.crearProducto(dto)));
    }

    @PutMapping("/api/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable int id,
                                                               @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(ProductoResponse.de(service.actualizarProducto(id, dto)));
    }

    @DeleteMapping("/api/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /* ---------- Medicamentos y vacunas ---------- */

    @GetMapping({"/api/medicamentos", "/api/medicamentos-vacunas"})
    public List<MedicamentoResponse> listarMedicamentos(@RequestParam(required = false) String categoria) {
        return categoria == null
                ? service.listarMedicamentos().stream().map(MedicamentoResponse::de).toList()
                : service.listarMedicamentosPorCategoria(categoria).stream().map(MedicamentoResponse::de).toList();
    }

    @GetMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    public MedicamentoResponse buscarMedicamento(@PathVariable String codigo) {
        return MedicamentoResponse.de(service.buscarMedicamento(codigo));
    }

    @PostMapping({"/api/medicamentos", "/api/medicamentos-vacunas"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicamentoResponse> crearMedicamento(@Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MedicamentoResponse.de(service.crearMedicamento(dto)));
    }

    @PutMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicamentoResponse> actualizarMedicamento(@PathVariable String codigo,
                                                                     @Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.ok(MedicamentoResponse.de(service.actualizarMedicamento(codigo, dto)));
    }

    @DeleteMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable String codigo) {
        service.eliminarMedicamento(codigo);
        return ResponseEntity.noContent().build();
    }
}
