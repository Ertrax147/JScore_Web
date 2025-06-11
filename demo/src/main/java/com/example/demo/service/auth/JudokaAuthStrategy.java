package com.example.demo.service.auth;

import lombok.RequiredArgsConstructor;
import com.example.demo.service.JudokaService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JudokaAuthStrategy implements AuthenticationStrategy{

    private final JudokaService judokaService;
    @Override
    public boolean authenticate(String username, String password) {
        return judokaService.validarContrasena(username, password);
    }

    @Override
    public String getTipo() {
        return "judoka";
    }

}
