package com.example.demo;

// Archivo: src/test/java/com/example/demo/controllerweb/AuthControllerTest.java
// Tipo: Prueba de la Capa Web (Controlador)


import com.example.demo.controllerweb.AuthController;
import com.example.demo.service.ClubService;
import com.example.demo.service.JudokaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba para AuthController.
 * @WebMvcTest carga únicamente la capa web (el controlador especificado) sin
 * cargar todo el contexto de la aplicación. Es más rápida que @SpringBootTest.
 * Los servicios son "mockeados" (simulados) con @MockBean.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP.

    @MockBean
    private JudokaService judokaService; // Mock del servicio de Judoka.

    @MockBean
    private ClubService clubService; // Mock del servicio de Club.

    @Test
    void testDoLoginExitosoJudoka() throws Exception {
        // Arrange: Definimos el comportamiento del mock.
        // Cuando se llame a judokaService.validarContrasena con estos datos, debe devolver true.
        when(judokaService.validarContrasena("judoka@mail.com", "password")).thenReturn(true);

        // Act & Assert: Realizar una petición POST a /login y verificar el resultado.
        mockMvc.perform(post("/login")
                        .param("username", "judoka@mail.com")
                        .param("password", "password")
                        .param("tipo", "judoka"))
                .andExpect(status().is3xxRedirection()) // Esperamos un estado de redirección (302).
                .andExpect(redirectedUrl("/judoka/home")); // Esperamos que redirija a la home del judoka.
    }

    @Test
    void testDoLoginFallido() throws Exception {
        // Arrange: Configuramos el mock para que la validación falle.
        when(judokaService.validarContrasena("judoka@mail.com", "wrongpassword")).thenReturn(false);

        // Act & Assert: Realizamos la petición con contraseña incorrecta.
        mockMvc.perform(post("/login")
                        .param("username", "judoka@mail.com")
                        .param("password", "wrongpassword")
                        .param("tipo", "judoka"))
                .andExpect(status().isOk()) // Esperamos un estado 200 OK (no redirección).
                .andExpect(view().name("Model/login")) // Esperamos que nos devuelva a la vista de login.
                .andExpect(model().attributeExists("error")); // Esperamos que el modelo contenga un atributo "error".
    }

    @Test
    void testDoLoginUsuarioVacio() throws Exception {
        // Act & Assert: Realizamos la petición con el campo username vacío.
        mockMvc.perform(post("/login")
                        .param("username", "")
                        .param("password", "password")
                        .param("tipo", "judoka"))
                .andExpect(status().isOk())
                .andExpect(view().name("Model/login"))
                .andExpect(model().attribute("error", "Usuario vacío"));
    }
}