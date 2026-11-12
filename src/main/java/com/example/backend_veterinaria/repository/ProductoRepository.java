package com.example.backend_veterinaria.repository;
import com.example.backend_veterinaria.model.MedicamentosVacunas;
import com.example.backend_veterinaria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByCategoria(String categoria);
}
