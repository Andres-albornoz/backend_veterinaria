package com.example.backend_veterinaria.service;

import com.example.backend_veterinaria.dto.UsuarioResponse;
import com.example.backend_veterinaria.dto.UsuarioUpdateDTO;
import com.example.backend_veterinaria.exception.ConflictoException;
import com.example.backend_veterinaria.exception.RecursoNoEncontradoException;
import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.repository.UserRepository;
import com.example.backend_veterinaria.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public void register(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Email y password son obligatorios");
        }
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

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return userRepository.findAll().stream().map(UsuarioResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Integer id) {
        return UsuarioResponse.de(buscarUser(id));
    }

    @Transactional
    public UsuarioResponse actualizar(Integer id, UsuarioUpdateDTO dto) {
        User user = buscarUser(id);
        userRepository.findByEmail(dto.email())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ConflictoException("El email ya existe");
                });
        user.setNombre(dto.nombre());
        user.setEmail(dto.email());
        user.setRol(validarRol(dto.rol()));
        return UsuarioResponse.de(userRepository.save(user));
    }

    @Transactional
    public void eliminar(Integer id) {
        User user = buscarUser(id);
        userRepository.delete(user);
    }

    private User buscarUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private String validarRol(String rol) {
        if (!"ADMIN".equalsIgnoreCase(rol) && !"USER".equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("El rol debe ser ADMIN o USER");
        }
        return rol.toUpperCase();
    }
}
