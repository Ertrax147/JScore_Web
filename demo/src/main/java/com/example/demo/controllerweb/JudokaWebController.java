package com.example.demo.controllerweb;

import com.example.demo.model.logger.LoggerManager;
import com.example.demo.model.user.Judoka;
import com.example.demo.service.JudokaService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // ¡NUEVA IMPORTACIÓN!
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional; // ¡NUEVA IMPORTACIÓN!
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Controller
public class JudokaWebController {
    private static final Logger logger = LoggerManager.getLogger(JudokaWebController.class);
    private final JudokaService judokaService;

    @GetMapping("/judokas")
    public String listarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        if (judokas.isEmpty()) {
            logger.log(Level.INFO, "No hay judokas registradas");
        }
        model.addAttribute("judokas", judokas);
        return "Judoka/judokas";
    }

    @PostMapping("/judokas")
    public String mostrarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return "Judoka/judokas";
    }

    private boolean esJudoka(HttpSession s) {
        return s.getAttribute("username") != null && "judoka".equals(s.getAttribute("tipo"));
    }

//    @GetMapping("/judoka/home")
//    public String judokaHome(HttpSession session, Model model) {
//        if (!esJudoka(session)) return "redirect:/login";
//        String username = (String) session.getAttribute("username");
//
//        Judoka judoka = judokaService.findByUsername(username).orElse(null);
//        if (judoka != null) {
//            model.addAttribute("nombre", judoka.getNombre());
//        } else {
//            model.addAttribute("nombre", username);
//        }
//        return "Judoka/judoka_home";
//    }

    // --- NUEVO MÉTODO PARA VER EL PERFIL DEL JUDOKA ---
    @GetMapping("/judoka/perfil/{id}")
    public String verPerfilJudoka(@PathVariable("id") Long id, Model model, HttpSession session) {

        Optional<Judoka> judokaOpt = judokaService.findById(id); // Asume que tienes judokaService.findById(id)

        if (judokaOpt.isPresent()) {
            model.addAttribute("judoka", judokaOpt.get());
            return "Judoka/perfil_judoka"; // Nombre de tu plantilla de perfil
        } else {
            logger.log(Level.WARNING, "Intento de acceso a perfil de judoka no encontrado con ID: " + id);
            // Redirige a la lista de judokas con un parámetro de error
            return "redirect:/judokas?error=noEncontrado";
        }
    }

    @GetMapping("/registro-judoka")
    public String showRegistroJudoka(Model model) { // Añadimos el parámetro Model
        // Creamos la lista de categorías de peso
        List<String> categoriasDePeso = Arrays.asList(
                "-60 kg",
                "-66 kg",
                "-73 kg",
                "-81 kg",
                "-90 kg",
                "-100 kg",
                "+100 kg"
        );

        // Añadimos la lista al modelo para que la vista pueda usarla
        model.addAttribute("categorias", categoriasDePeso);

        return "Judoka/registro_judoka"; // El nombre de tu vista
    }

    @PostMapping("/registro-judoka")
    public String doRegistroJudoka(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String categoria,
            @RequestParam String fechaNacimiento,
            Model model
    ) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                nombre == null || nombre.isBlank() ||
                apellido == null || apellido.isBlank() ||
                categoria == null || categoria.isBlank() ||
                fechaNacimiento == null || fechaNacimiento.isBlank()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "Judoka/registro_judoka";
        }

        if (judokaService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "Judoka/registro_judoka";
        }

        Judoka nuevo = new Judoka();
        nuevo.setUsername(username);
        nuevo.setPassword(password); // ¡NO GUARDAR EN TEXTO PLANO EN PRODUCCIÓN!
        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setCategoria(categoria);
        nuevo.setFechaNacimiento(fechaNacimiento);

        judokaService.guardarJudoka(nuevo);
        model.addAttribute("success", "¡Judoka registrado correctamente! Ahora puedes iniciar sesión.");
        return "Judoka/registro_judoka";
    }
}
