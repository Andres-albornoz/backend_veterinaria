package com.example.backend_veterinaria.repository;
import com.example.backend_veterinaria.model.MedicamentosVacunas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductoRepository {
    @Repository
    public interface ProductoRepository extends JpaRepository<MedicamentosVacunas, String> {

        // Método personalizado para filtrar por categoría si lo necesitas
        List<MedicamentosVacunas> findByCategoria(String categoria);
    }

}
