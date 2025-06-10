package com.example.demo;

import com.example.demo.model.user.Judoka;
import com.example.demo.repository.JudokaRepository;
import com.example.demo.service.JudokaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración para JudokaService.
 * @SpringBootTest carga el contexto completo de la aplicación, permitiendo
 * probar la interacción entre el servicio y el repositorio con una BBDD en memoria.
 */
@SpringBootTest
class JudokaServiceTest {

    @Autowired
    private JudokaService judokaService;

    @Autowired
    private JudokaRepository judokaRepository;

    // Limpia la base de datos después de cada prueba para asegurar que los tests sean independientes.
    @AfterEach
    void tearDown() {
        judokaRepository.deleteAll();
    }

    @Test
    void testGuardarYBuscarJudoka() {
        // Arrange: Crear una nueva instancia de Judoka.
        Judoka nuevoJudoka = new Judoka();
        nuevoJudoka.setUsername("test.judoka@mail.com");
        nuevoJudoka.setPassword("password123");
        nuevoJudoka.setNombre("Test");
        nuevoJudoka.setApellido("Judoka");
        nuevoJudoka.setCategoria("61KG");
        nuevoJudoka.setFechaNacimiento("2000-01-01");

        // Act: Guardar el judoka usando el servicio.
        judokaService.guardarJudoka(nuevoJudoka);

        // Assert: Buscar el judoka por su username y verificar que los datos coinciden.
        Optional<Judoka> judokaGuardadoOpt = judokaService.findByUsername("test.judoka@mail.com");

        assertTrue(judokaGuardadoOpt.isPresent(), "El judoka debería haber sido encontrado en la BBDD.");
        Judoka judokaGuardado = judokaGuardadoOpt.get();
        assertEquals("Test", judokaGuardado.getNombre());
        assertEquals("61KG", judokaGuardado.getCategoria());
    }

    @Test
    void testValidarContrasena() {
        // Arrange: Guardar un judoka de prueba.
        Judoka judoka = new Judoka();
        judoka.setUsername("user@test.com");
        judoka.setPassword("pass_secreta"); // En un proyecto real, la contraseña estaría hasheada.
        judokaRepository.save(judoka);

        // Act & Assert
        assertTrue(judokaService.validarContrasena("user@test.com", "pass_secreta"), "La contraseña válida debería retornar true.");
        assertFalse(judokaService.validarContrasena("user@test.com", "pass_incorrecta"), "La contraseña incorrecta debería retornar false.");
        assertFalse(judokaService.validarContrasena("no_existe@test.com", "pass_secreta"), "Un usuario que no existe debería retornar false.");
    }
}