package com.example.demo.service.auth;

import lombok.RequiredArgsConstructor;
import com.example.demo.service.ClubService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubAuthStrategy implements com.example.demo.service.auth.AuthenticationStrategy {

    private final ClubService clubService;

    @Override
    public boolean authenticate(String username, String password) {
        return clubService.validarContrasena(username, password);
    }

    @Override
    public String getTipo() {
        return "club";
    }


}
