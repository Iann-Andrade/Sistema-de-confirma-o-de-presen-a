package com.Lista_de_Presenca.Fisioterapia.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/teste")
    public String teste(Authentication authentication) {

        if (authentication == null) {
            return "Não autenticado";
        }

        return "Autenticado! Usuário: " + authentication.getName();
    }
}

