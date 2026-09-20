package com.Lista_de_Presenca.Fisioterapia.controller;


import com.Lista_de_Presenca.Fisioterapia.dto.LoginRequest;
import com.Lista_de_Presenca.Fisioterapia.model.Usuario;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;
import com.Lista_de_Presenca.Fisioterapia.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Lista_de_Presenca.Fisioterapia.dto.LoginResponse;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastrar")
    public Usuario cadastrar(@RequestBody Usuario usuario) {
        return usuarioService.cadastrar(usuario);
    }

    //Logar usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try{
            Usuario usuarioLogado = usuarioService.login(
                request.getEmail(),
                request.getSenha()
            );

            String roleName = (usuarioLogado.getRole() != null) ? usuarioLogado.getRole().name() : "USER";

            String token = jwtService.gerarToken(
                usuarioLogado.getId(),
                roleName
            );

            LoginResponse resposta = new LoginResponse(
                usuarioLogado.getId(),
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                roleName,
                token
            );

            return ResponseEntity.ok(resposta);

        }catch(RuntimeException e){
            System.out.println("Falha no login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

     
}
