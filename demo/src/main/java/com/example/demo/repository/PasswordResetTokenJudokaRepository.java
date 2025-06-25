package com.example.demo.repository;

import com.example.demo.model.user.Judoka;
import com.example.demo.model.user.PasswordResetTokenJudoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJudokaRepository extends JpaRepository<PasswordResetTokenJudoka, Long> {
    Optional<PasswordResetTokenJudoka> findByToken(String token);
    Optional<PasswordResetTokenJudoka> findByJudoka(Judoka judoka);

}
