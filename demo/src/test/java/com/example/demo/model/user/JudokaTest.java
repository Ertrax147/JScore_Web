package com.example.demo.model.user;

// Tipo: Prueba Unitaria
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba unitaria para la clase Judoka.
 * No requiere el contexto de Spring, ya que prueba lógica pura del modelo.
 */
public class JudokaTest {

    @Test
    void testCalcularPorcentajeVictorias_ConDatosMezclados() {
        // Arrange: Crear un judoka con un historial de combates.
        Judoka judoka = new Judoka();
        judoka.setVictorias(1);
        judoka.setDerrotas(1);
        judoka.setEmpates(2); // Total de 4 combates

        // Act: Calcular el porcentaje de victorias.
        double porcentaje = judoka.calcularPorcentajeVictorias();

        // Assert: Verificar que el resultado es el esperado (1 de 4 es 25%).
        assertEquals(25.0, porcentaje, 0.01, "El porcentaje de victorias debería ser 25.0%");
    }

    @Test
    void testCalcularPorcentajeVictorias_SinCombates() {
        // Arrange: Crear un judoka sin combates.
        Judoka judoka = new Judoka();

        // Act: Calcular el porcentaje.
        double porcentaje = judoka.calcularPorcentajeVictorias();

        // Assert: Verificar que el resultado es 0 para evitar división por cero.
        assertEquals(0.0, porcentaje, 0.01, "El porcentaje debería ser 0 si no hay combates.");
    }

    @Test
    void testCalcularPorcentajeVictorias_SoloVictorias() {
        // Arrange: Crear un judoka con solo victorias.
        Judoka judoka = new Judoka();
        judoka.setVictorias(5);

        // Act: Calcular el porcentaje.
        double porcentaje = judoka.calcularPorcentajeVictorias();

        // Assert: Verificar que el resultado es 100%.
        assertEquals(100.0, porcentaje, 0.01, "El porcentaje debería ser 100% si solo hay victorias.");
    }


}
