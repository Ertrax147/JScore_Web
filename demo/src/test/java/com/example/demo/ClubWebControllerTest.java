package com.example.demo;

import com.example.demo.controllerweb.ClubWebController;
import com.example.demo.model.user.Club;
import com.example.demo.service.ClubService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClubWebControllerTest {

    private ClubWebController clubWebController;

    @Mock
    private ClubService clubService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clubWebController = new ClubWebController(clubService);
    }

    /**
     * Prueba que el método clubHome redirija al inicio de sesión si no es un club válido.
     */
    @Test
    void testClubHomeDireccionaAlLogin_siNoEsValidoElClub() {
        when(session.getAttribute("username")).thenReturn(null);
        when(session.getAttribute("tipo")).thenReturn(null);

        String result = clubWebController.clubHome(session, model);

        assertEquals("redirect:/login", result);
        verify(model, never()).addAttribute(anyString(), any());
    }

    /**
     * Prueba que el método clubHome muestra el nombre del club correctamente cuando
     * un club válido está logueado.
     */
    @Test
    void testClubHomeMuestraElNombreDelClubParaUnLoginValido() {
        when(session.getAttribute("username")).thenReturn("club123");
        when(session.getAttribute("tipo")).thenReturn("club");
        Club club = new Club();
        club.setNombre("Club ABC");

        when(clubService.findByUsername("club123")).thenReturn(Optional.of(club));

        String result = clubWebController.clubHome(session, model);

        assertEquals("Club/club_home", result);
        verify(model).addAttribute("nombre", "Club ABC");
    }

    /**
     * Prueba que el método clubHome utiliza el nombre de usuario como fallback
     * si no encuentra el club.
     */
    @Test
    void TestClubHomeNombreUsuarioComoFallback() {
        when(session.getAttribute("username")).thenReturn("club123");
        when(session.getAttribute("tipo")).thenReturn("club");

        when(clubService.findByUsername("club123")).thenReturn(Optional.empty());

        String result = clubWebController.clubHome(session, model);

        assertEquals("Club/club_home", result);
        verify(model).addAttribute("nombre", "club123");
    }

    /**
     * Prueba que el método listarClubes carga una lista de clubes y muestra la vista correcta.
     */
    @Test
    void testListarClubes() {
        List<Club> clubes = List.of(new Club(), new Club());
        when(clubService.getAllClubs()).thenReturn(clubes);

        String result = clubWebController.listarClubes(model);

        assertEquals("Club/club_lista", result);
        verify(model).addAttribute("clubes", clubes);
    }

    /**
     * Prueba que el método showRegistroClub devuelva la vista de registro de clubes.
     */
    @Test
    void testMostrarRegistroClub() {
        String result = clubWebController.showRegistroClub();

        assertEquals("Club/registro_club", result);
    }

    /**
     * Prueba el registro de un club con datos válidos.
     */
    @Test
    void testDoRegistroClubExitoso() {
        // Arrange
        when(clubService.findByUsername("club123@test.com")).thenReturn(Optional.empty());

        // Act
        String result = clubWebController.doRegistroClub("club123@test.com", "password123", "Club ABC", model);

        // Assert
        // 1. Verifica que la vista devuelta es la del formulario de registro
        assertEquals("Club/registro_club", result);

        // 2. Verifica que se llamó al método para guardar el club
        verify(clubService).guardarClub(any(Club.class));

        // 3. (Opcional pero muy bueno) Verifica que se añadió un mensaje de éxito al modelo
        verify(model).addAttribute(eq("success"), anyString());
    }

    /**
     * Prueba el registro de un club con campos obligatorios vacíos.
     */
    @Test
    void testDoRegistroClubSinUsername() {
        String result = clubWebController.doRegistroClub("", "password123", "Club ABC", model);

        assertEquals("Club/registro_club", result);
        verify(model).addAttribute("error", "Todos los campos son obligatorios.");
        verify(clubService, never()).guardarClub(any(Club.class));
    }

    /**
     * Prueba el registro de un club cuando el nombre de usuario ya está registrado.
     */
    @Test
    void testDoRegistroClubConUsernameYaRegistrado() {
        when(clubService.findByUsername("club123@test.com")).thenReturn(Optional.of(new Club()));

        String result = clubWebController.doRegistroClub("club123@test.com", "password123", "Club ABC", model);

        assertEquals("Club/registro_club", result);
        verify(model).addAttribute("error", "El correo ya está registrado para un club.");
        verify(clubService, never()).guardarClub(any(Club.class));
    }
}