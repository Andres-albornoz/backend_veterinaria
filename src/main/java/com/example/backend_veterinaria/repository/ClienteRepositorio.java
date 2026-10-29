package com.example.backend_veterinaria.repository;

import com.example.backend_veterinaria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ClienteRepositorio extends  JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}