package com.example.backend_veterinaria;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
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

    private String nuevoUsuario() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@mail.cl";
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"User\",\"email\":\"" + email
                                + "\",\"password\":\"clave123\"}"))
                .andExpect(status().isCreated());
        return email;
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

        mvc.perform(get("/api/service/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(id));

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

        String userToken = token("{\"email\":\"" + nuevoUsuario() + "\",\"password\":\"clave123\"}");

        mvc.perform(post("/api/service")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SERVICIO))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/service").header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void crudDeProductosConAdministrador() throws Exception {
        String token = token(ADMIN_LOGIN);
        String nombre = "Alimento " + UUID.randomUUID();
        String body = "{\"nombre\":\"" + nombre + "\",\"descripcion\":\"Desc\",\"precio\":12000,"
                + "\"stock\":7,\"activo\":true}";

        MvcResult creado = mvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(nombre))
                .andExpect(jsonPath("$.stock").value(7))
                .andExpect(jsonPath("$.activo").value(true))
                .andReturn();
        int id = Integer.parseInt(creado.getResponse().getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1"));

        mvc.perform(get("/api/productos/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Desc"));

        mvc.perform(get("/api/productos").param("nombre", nombre)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mvc.perform(put("/api/productos/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("\"stock\":7", "\"stock\":9")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(9));

        mvc.perform(delete("/api/productos/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/productos/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void crudDeMedicamentosConAdministrador() throws Exception {
        String token = token(ADMIN_LOGIN);
        String codigo = "MED-" + UUID.randomUUID();
        String body = "{\"codigo\":\"" + codigo + "\",\"categoria\":\"Vacuna\","
                + "\"nombre\":\"Vacuna antirrabica\",\"principioActivo\":\"Antigeno\","
                + "\"presentacion\":\"1 dosis\",\"especie\":\"Perro\",\"stock\":12,\"precio\":25000}";

        mvc.perform(post("/api/medicamentos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value(codigo))
                .andExpect(jsonPath("$.categoria").value("Vacuna"));

        mvc.perform(get("/api/medicamentos").param("categoria", "Vacuna")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.codigo == '" + codigo + "')]").exists());

        mvc.perform(put("/api/medicamentos/" + codigo)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("\"stock\":12", "\"stock\":20")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(20));

        mvc.perform(delete("/api/medicamentos/" + codigo).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/medicamentos/" + codigo).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void validacionYAccesoDeCatalogo() throws Exception {
        String adminToken = token(ADMIN_LOGIN);
        String userToken = token("{\"email\":\"" + nuevoUsuario() + "\",\"password\":\"clave123\"}");

        mvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"precio\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nombre").exists());

        mvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Producto\",\"precio\":10}"))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/medicamentos")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"X\",\"nombre\":\"X\",\"stock\":1,\"precio\":1}"))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/productos").header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void crudDeUsuariosConAdministrador() throws Exception {
        String token = token(ADMIN_LOGIN);
        String email = "gestion-" + UUID.randomUUID() + "@mail.cl";
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Gestion\",\"email\":\"" + email
                                + "\",\"password\":\"clave123\"}"))
                .andExpect(status().isCreated());

        MvcResult listado = mvc.perform(get("/api/users").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        List<Integer> ids = JsonPath.read(listado.getResponse().getContentAsString(),
                "$[?(@.email == '" + email + "')].id");
        assertTrue(ids != null && !ids.isEmpty(), "El usuario creado debe aparecer en el listado");
        int id = ids.get(0);

        mvc.perform(get("/api/users/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.rol").value("USER"));

        mvc.perform(put("/api/users/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Gestion 2\",\"email\":\"" + email + "\",\"rol\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gestion 2"));

        mvc.perform(delete("/api/users/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/users/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
