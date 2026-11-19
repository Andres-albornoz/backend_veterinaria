package com.example.backend_veterinaria.service;

import com.example.backend_veterinaria.dto.ServiciosDTO;
import com.example.backend_veterinaria.exception.RecursoNoEncontradoException;
import com.example.backend_veterinaria.model.Servicios;
import com.example.backend_veterinaria.repository.ServiciosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiciosService {

    private final ServiciosRepository repository;

    public ServiciosService(ServiciosRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Servicios> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Servicios> listarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public Servicios buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id));
    }

    @Transactional
    public Servicios crear(ServiciosDTO dto) {
        Servicios servicio = new Servicios(dto.categoria(), dto.nombre(), dto.especie(),
                dto.duracion(), dto.precio(), dto.observaciones());
        return repository.save(servicio);
    }

    @Transactional
    public Servicios actualizar(Long id, ServiciosDTO dto) {
        Servicios servicio = buscarPorId(id);
        servicio.setCategoria(dto.categoria());
        servicio.setNombre(dto.nombre());
        servicio.setEspecie(dto.especie());
        servicio.setDuracion(dto.duracion());
        servicio.setPrecio(dto.precio());
        servicio.setObservaciones(dto.observaciones());
        return repository.save(servicio);
    }

    @Transactional
    public void eliminar(Long id) {
        Servicios servicio = buscarPorId(id);
        repository.delete(servicio);
    }
}
