package com.example.backend_veterinaria.config;

import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.model.Producto;
import com.example.backend_veterinaria.repository.UserRepository;
import com.example.backend_veterinaria.repository.ProductoRepository;
import com.example.backend_veterinaria.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository User, ProductoRepository productos, PasswordEncoder enc) {
        return args -> {
            if (User.findByEmail("admin@admin.cl").isEmpty()) {
                User.save(new User(0, "admin", "admin@admin.cl", enc.encode("admin123"), "ADMIN"));
            }
            if (User.findByEmail("user@user.cl").isEmpty()) {
                User.save(new User(1, "user","user@user.cl", enc.encode("user123"), "USER"));
            }
            if (productos.count() == 0) {
                productos.save(new Producto(01, "correaA", "correaA", 1234, 10, true));
                productos.save(new Producto(02, "correaB", "correaB", 1234, 10, true));
                productos.save(new Producto(03, "correaC", "correaC", 1234, 10, true));
            }
        };
    }
}
