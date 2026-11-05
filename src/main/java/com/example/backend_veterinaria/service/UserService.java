package com.example.backend_veterinaria.service;

import com.example.backend_veterinaria.model.User;
import com.example.backend_veterinaria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("El email ya existe");
        }

        if (user.getRol() == null || user.getRol().isEmpty()){
            user.setRol("USER");
        }
        userRepository.save(user);
    }

    public Map<String, String> login(User userLogin){
        Optional<User> userOptional = userRepository.findByEmail(userLogin.getEmail());

        if(userOptional.isPresent() && userOptional.get().getPassword().equals(userLogin.getPassword())){
            User user = userOptional.get();

            Map<String, String> response = new HashMap<>();
            response.put("token", "jwt-simulado-" + UUID.randomUUID().toString());
            response.put("email", user.getEmail());
            response.put("roles", user.getRol());

            return response;
        }
        throw new RuntimeException("No autorizado");
    }
}
