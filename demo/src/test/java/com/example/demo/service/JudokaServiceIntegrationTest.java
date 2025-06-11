package com.example.demo.service;

import com.example.demo.model.user.Judoka;
import com.example.demo.repository.JudokaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JudokaServiceIntegrationTest {

    @Autowired
    private JudokaService judokaService;

    @Autowired
    private JudokaRepository judokaRepository;

    private Judoka judokaDePrueba;

    @BeforeEach
    void setUp() {
        judokaRepository.deleteAll(); // Limpiamos por si acaso

        judokaDePrueba = new Judoka();
        judokaDePrueba.setUsername("test.judoka@mail.com");
        judokaDePrueba.setPassword("password123");
        judokaDePrueba.setNombre("Test");
        judokaDePrueba.setApellido("Judoka");
        // Guardamos la entidad para que exista en la BD para los tests de update y delete
        judokaDePrueba = judokaRepository.save(judokaDePrueba);
    }

    //Limpia la base de datos después de cada prueba
    @AfterEach
    void tearDown() {
        judokaRepository.deleteAll();
    }

    @Test
    void testGuardarYBuscarJudoka() {
        // Arrange
        Judoka nuevo = new Judoka();
        nuevo.setUsername("nuevo.judoka@mail.com");
        nuevo.setNombre("Nuevo");
        nuevo.setApellido("Atleta");

        // Guardamos y buscamos
        judokaService.guardarJudoka(nuevo);
        Optional<Judoka> guardado = judokaService.findByUsername("nuevo.judoka@mail.com");

        // Assert
        assertTrue(guardado.isPresent(), "El nuevo judoka debería existir en la BD.");
        assertEquals("Nuevo", guardado.get().getNombre());
    }

    @Test
    void testEliminarJudoka() {
        Long idDelJudoka = judokaDePrueba.getId();
        assertTrue(judokaService.findById(idDelJudoka).isPresent(), "El judoka debe existir.");

        // Eliminamos el judoka a través del servicio
        judokaService.eliminarJudoka(idDelJudoka);

        // Assert
        // Verificamos que al buscarlo de nuevo, ya no se encuentra
        Optional<Judoka> judokaEliminadoOpt = judokaService.findById(idDelJudoka);
        assertFalse(judokaEliminadoOpt.isPresent(), "El judoka no debería existir.");
    }

    @Test
    void testActualizarJudoka() {
        // Actualizamos un dato
        judokaDePrueba.aumentarVictoria(); // Ahora tiene 1 victoria

        // Act
        // Guardamos los cambios
        judokaService.guardarJudoka(judokaDePrueba);

        // Recuperamos la entidad de la BD para verificar el cambio
        Optional<Judoka> judokaActualizadoOpt = judokaService.findById(judokaDePrueba.getId());

        // Assert
        assertTrue(judokaActualizadoOpt.isPresent());
        assertEquals(1, judokaActualizadoOpt.get().getVictorias(), "El número de victorias debería ser 1.");
    }
}