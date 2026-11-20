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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(
        name = "catalogo",
        description = "Operaciones relacionadas con el catalogo"
)
public class CatalogoController {

    private final CatalogoService service;

    public CatalogoController(CatalogoService service) {
        this.service = service;
    }

    @GetMapping("/api/productos")
    @Operation(
            summary = "Listar Productos",
            description = "Obtiene todos los productos registrados"
    )
    public List<ProductoResponse> listarProductos(@RequestParam(required = false) String nombre) {
        return nombre == null
                ? service.listarProductos().stream().map(ProductoResponse::de).toList()
                : service.buscarProductosPorNombre(nombre).stream().map(ProductoResponse::de).toList();
    }

    @GetMapping("/api/productos/{id}")
    @Operation(
            summary = "buscar Producto",
            description = "busca un producto por su id"
    )
    public ProductoResponse buscarProducto(@PathVariable int id) {
        return ProductoResponse.de(service.buscarProducto(id));
    }

    @PostMapping("/api/productos")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "crear Producto",
            description = "permite crear un nuevo producto"
    )
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoResponse.de(service.crearProducto(dto)));
    }

    @PutMapping("/api/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "actualizar Producto",
            description = "actualiza los datos de un producto"
    )
    public ResponseEntity<ProductoResponse> actualizarProducto(@PathVariable int id,
                                                               @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(ProductoResponse.de(service.actualizarProducto(id, dto)));
    }

    @DeleteMapping("/api/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "eliminar Producto",
            description = "elimina un producto del catalogo por su id"
    )
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping({"/api/medicamentos", "/api/medicamentos-vacunas"})
    @Operation(
            summary = "listar Medicamentos",
            description = "lista todos los medicamentos registrados"
    )
    public List<MedicamentoResponse> listarMedicamentos(@RequestParam(required = false) String categoria) {
        return categoria == null
                ? service.listarMedicamentos().stream().map(MedicamentoResponse::de).toList()
                : service.listarMedicamentosPorCategoria(categoria).stream().map(MedicamentoResponse::de).toList();
    }

    @GetMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    @Operation(
            summary = "buscar Medicamento",
            description = "busca un medicamento por su código"
    )
    public MedicamentoResponse buscarMedicamento(@PathVariable String codigo) {
        return MedicamentoResponse.de(service.buscarMedicamento(codigo));
    }

    @PostMapping({"/api/medicamentos", "/api/medicamentos-vacunas"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "crear Medicamento",
            description = "permite crear un nuevo medicamento"
    )
    public ResponseEntity<MedicamentoResponse> crearMedicamento(@Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MedicamentoResponse.de(service.crearMedicamento(dto)));
    }

    @PutMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "actualizar Medicamento",
            description = "actualiza los datos de un medicamento"
    )
    public ResponseEntity<MedicamentoResponse> actualizarMedicamento(@PathVariable String codigo,
                                                                     @Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.ok(MedicamentoResponse.de(service.actualizarMedicamento(codigo, dto)));
    }

    @DeleteMapping({"/api/medicamentos/{codigo}", "/api/medicamentos-vacunas/{codigo}"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "eliminar Medicamento",
            description = "elimina un medicamento del catálogo por su código"
    )
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable String codigo) {
        service.eliminarMedicamento(codigo);
        return ResponseEntity.noContent().build();
    }
}
