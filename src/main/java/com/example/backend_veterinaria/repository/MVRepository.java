package com.example.backend_veterinaria.repository;

import com.example.backend_veterinaria.model.MedicamentosVacunas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MVRepository extends JpaRepository<MedicamentosVacunas, Long> {
}