package com.example.backend_veterinaria.repository;

import com.example.backend_veterinaria.model.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiciosRepository extends JpaRepository<Servicios, Long> {
    List<Servicios> findByCategoria(String categoria);
}
