package com.example.backend_veterinaria.config;

import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.model.Producto;
import com.example.backend_veterinaria.repository.UserRepository;
import com.example.backend_veterinaria.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository usuarios, ProductoRepository productos, PasswordEncoder enc) {
        return args -> {
            if (usuarios.findByEmail("admin@admin.cl").isEmpty()) {
                usuarios.save(new User(null, "admin", "admin@admin.cl", enc.encode("admin123"), "ADMIN"));
            }
            if (usuarios.findByEmail("user@user.cl").isEmpty()) {
                usuarios.save(new User(null, "user", "user@user.cl", enc.encode("user123"), "USER"));
            }
            if (productos.count() == 0) {
                productos.save(new Producto(0, "correaA", "correaA", 1234, 10, true));
                productos.save(new Producto(0, "correaB", "correaB", 1234, 10, true));
                productos.save(new Producto(0, "correaC", "correaC", 1234, 10, true));
            }
        };
    }
}
