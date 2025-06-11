package com.example.demo.repository;

import com.example.demo.model.user.Club;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Anotación clave para probar SOLO la capa JPA
@DataJpaTest
class ClubRepositoryTest {

    // TestEntityManager es una utilidad para manejar entidades en un contexto de prueba
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClubRepository clubRepository;

    @Test
    void findByUsername_cuandoClubExiste_debeRetornarElClub() {
        // Creamos  una entidad de prueba
        Club nuevoClub = new Club();
        nuevoClub.setUsername("test.club@kodokan.com");
        nuevoClub.setNombre("Kodokan Institute");
        entityManager.persist(nuevoClub);
        entityManager.flush(); // Forzamos la sincronización con la BD de prueba

        // Ejecutamos el método del repositorio que queremos probar
        Optional<Club> clubEncontrado = clubRepository.findByUsername("test.club@kodokan.com");

        // Assert
        assertTrue(clubEncontrado.isPresent(),
                "Se espera encontrar el club por su username.");
        assertEquals("Kodokan Institute", clubEncontrado.get().getNombre());
    }

    @Test
    void findByUsername_cuandoClubNoExiste_debeRetornarVacio() {
        // 1. Arrange

        // 2. Act
        Optional<Club> clubEncontrado = clubRepository.findByUsername("no.existe@mail.com");

        // 3. Assert
        assertFalse(clubEncontrado.isPresent(),
                "No se debería encontrar un club que no existe.");
    }
}