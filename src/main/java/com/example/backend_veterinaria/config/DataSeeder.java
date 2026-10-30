package com.example.backend_veterinaria.config;

import com.example.backend_veterinaria.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    /*
    @Bean
    CommandLineRunner seed(UsuarioRepository usuarios, ProductoRepository productos, PasswordEncoder enc) {
        return args -> {
            if (usuarios.findByEmail("admin@techstore.cl").isEmpty()) {
                usuarios.save(new Usuario("admin@techstore.cl", enc.encode("admin123"), "ADMIN"));
            }
            if (usuarios.findByEmail("user@techstore.cl").isEmpty()) {
                usuarios.save(new Usuario("user@techstore.cl", enc.encode("user123"), "USER"));
            }
            if (productos.count() == 0) {
                productos.save(new Producto("Notebook", 599990));
                productos.save(new Producto("Mouse", 12990));
                productos.save(new Producto("Teclado", 24990));
            }
        };
    }
     */
}
