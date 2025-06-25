package com.example.demo.repository;

import com.example.demo.model.user.Club;
import com.example.demo.model.user.PasswordResetTokenClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PasswordResetTokenClubRepository extends JpaRepository<PasswordResetTokenClub, Long> {
    Optional<PasswordResetTokenClub> findByClub(Club club);
    Optional<PasswordResetTokenClub> findByToken(String token);
}
