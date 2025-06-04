package com.example.demo.repository;

import com.example.demo.model.competencia.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Competencia repository.
 */
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    /**
     * Find by nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    List<Torneo> findByNombre(String nombre);

}
