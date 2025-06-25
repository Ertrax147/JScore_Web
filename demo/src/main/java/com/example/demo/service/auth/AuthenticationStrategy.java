package com.example.demo.service.auth;

public interface AuthenticationStrategy {
    boolean authenticate(String username, String password);
    String getTipo();

}
