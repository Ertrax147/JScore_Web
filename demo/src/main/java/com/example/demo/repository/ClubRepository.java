package com.example.demo.repository;

import com.example.demo.model.user.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByUsername(String username);

}
