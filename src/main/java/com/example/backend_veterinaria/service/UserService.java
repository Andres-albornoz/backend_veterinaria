package com.example.backend_veterinaria.service;

import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.repository.UserRepository;
import com.example.backend_veterinaria.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        String rol = (user.getRol() == null || user.getRol().isBlank()) ? "USER" : user.getRol();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(rol)
                .build();
    }

    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRol("USER");
        userRepository.save(user);
    }

    public Map<String, String> login(User userLogin) {
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new RuntimeException("No autorizado"));
        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw new RuntimeException("No autorizado");
        }
        return Map.of(
                "token", jwtService.generarToken(loadUserByUsername(user.getEmail())),
                "email", user.getEmail(),
                "roles", user.getRol()
        );
    }
}
