package com.example.demo.repository;

import com.example.demo.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
/**
 * The interface Judoka repository.
 */
public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    Optional<Judoka> findByUsername(String username);
}
