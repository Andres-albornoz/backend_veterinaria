package com.example.backend_veterinaria.service;

import com.example.backend_veterinaria.dto.MedicamentoDTO;
import com.example.backend_veterinaria.dto.ProductoDTO;
import com.example.backend_veterinaria.exception.ConflictoException;
import com.example.backend_veterinaria.exception.RecursoNoEncontradoException;
import com.example.backend_veterinaria.model.MedicamentosVacunas;
import com.example.backend_veterinaria.model.Producto;
import com.example.backend_veterinaria.repository.MVRepository;
import com.example.backend_veterinaria.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final MVRepository medicamentoRepository;

    public CatalogoService(ProductoRepository productoRepository, MVRepository medicamentoRepository) {
        this.productoRepository = productoRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public Producto buscarProducto(int id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    @Transactional
    public Producto crearProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        aplicarProducto(producto, dto);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(int id, ProductoDTO dto) {
        Producto producto = buscarProducto(id);
        aplicarProducto(producto, dto);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminarProducto(int id) {
        Producto producto = buscarProducto(id);
        productoRepository.delete(producto);
    }

    private void aplicarProducto(Producto producto, ProductoDTO dto) {
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock() == null ? 0 : dto.stock());
        producto.setActivo(dto.activo() == null ? Boolean.TRUE : dto.activo());
    }

    /* ---------- Medicamentos y vacunas ---------- */

    @Transactional(readOnly = true)
    public List<MedicamentosVacunas> listarMedicamentos() {
        return medicamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MedicamentosVacunas> listarMedicamentosPorCategoria(String categoria) {
        return medicamentoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public MedicamentosVacunas buscarMedicamento(String codigo) {
        return medicamentoRepository.findById(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medicamento no encontrado: " + codigo));
    }

    @Transactional
    public MedicamentosVacunas crearMedicamento(MedicamentoDTO dto) {
        if (medicamentoRepository.existsById(dto.codigo())) {
            throw new ConflictoException("El codigo ya existe: " + dto.codigo());
        }
        MedicamentosVacunas medicamento = new MedicamentosVacunas();
        aplicarMedicamento(medicamento, dto);
        return medicamentoRepository.save(medicamento);
    }

    @Transactional
    public MedicamentosVacunas actualizarMedicamento(String codigo, MedicamentoDTO dto) {
        MedicamentosVacunas medicamento = buscarMedicamento(codigo);
        aplicarMedicamento(medicamento, dto);
        medicamento.setCodigo(codigo);
        return medicamentoRepository.save(medicamento);
    }

    @Transactional
    public void eliminarMedicamento(String codigo) {
        MedicamentosVacunas medicamento = buscarMedicamento(codigo);
        medicamentoRepository.delete(medicamento);
    }

    private void aplicarMedicamento(MedicamentosVacunas medicamento, MedicamentoDTO dto) {
        medicamento.setCodigo(dto.codigo());
        medicamento.setCategoria(dto.categoria());
        medicamento.setNombre(dto.nombre());
        medicamento.setPrincipioActivo(dto.principioActivo());
        medicamento.setPresentacion(dto.presentacion());
        medicamento.setEspecie(dto.especie());
        medicamento.setStock(dto.stock());
        medicamento.setPrecio(dto.precio());
    }
}
