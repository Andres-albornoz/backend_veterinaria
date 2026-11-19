package com.example.backend_veterinaria;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiFlowTest {

    private static final String ADMIN_LOGIN =
            "{\"email\":\"admin@admin.cl\",\"password\":\"admin123\"}";
    private static final String SERVICIO =
            "{\"categoria\":\"Consulta\",\"nombre\":\"Consulta general\",\"especie\":\"Perro\","
                    + "\"duracion\":30,\"precio\":15000,\"observaciones\":\"Sin observaciones\"}";

    @Autowired
    MockMvc mvc;

    private String token(String loginBody) throws Exception {
        MvcResult result = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"token\""), "La respuesta de login debe contener un token JWT");
        return body.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    @Test
    void loginAdminDevuelveToken() throws Exception {
        token(ADMIN_LOGIN);
    }

    @Test
    void loginConPasswordIncorrectaEs401() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@admin.cl\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registroDuplicadoEs409() throws Exception {
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"admin\",\"email\":\"admin@admin.cl\",\"password\":\"x\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void registroYLoginDeUsuario() throws Exception {
        String email = "test-" + UUID.randomUUID() + "@mail.cl";
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Prueba\",\"email\":\"" + email
                                + "\",\"password\":\"clave123\",\"rol\":\"ADMIN\"}"))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"clave123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").value("USER"));
    }

    @Test
    void crudDeServiciosConAdministrador() throws Exception {
        String token = token(ADMIN_LOGIN);

        MvcResult creado = mvc.perform(post("/api/service")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SERVICIO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Consulta general"))
                .andReturn();
        long id = Long.parseLong(creado.getResponse().getContentAsString()
                .replaceAll(".*\"codigo\":(\\d+).*", "$1"));

        mvc.perform(get("/api/service").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.codigo == " + id + ")]").exists());

        mvc.perform(put("/api/service/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SERVICIO.replace("Consulta general", "Consulta actualizada")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Consulta actualizada"));

        mvc.perform(delete("/api/service/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/service").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.codigo == " + id + ")]").doesNotExist());
    }

    @Test
    void sinTokenEs401YUsuarioNoAdminNoPuedeEscribir() throws Exception {
        mvc.perform(get("/api/service")).andExpect(status().isUnauthorized());

        String email = "user-" + UUID.randomUUID() + "@mail.cl";
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"User\",\"email\":\"" + email
                                + "\",\"password\":\"clave123\"}"))
                .andExpect(status().isCreated());
        String userToken = token("{\"email\":\"" + email + "\",\"password\":\"clave123\"}");

        mvc.perform(post("/api/service")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SERVICIO))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/service").header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}
