package com.example.backend_veterinaria.repository;

import com.example.backend_veterinaria.model.MedicamentosVacunas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MVRepository extends JpaRepository<MedicamentosVacunas, String> {
    List<MedicamentosVacunas> findByCategoria(String categoria);
}