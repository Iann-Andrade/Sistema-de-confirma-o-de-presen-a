package com.Lista_de_Presenca.Fisioterapia.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Lista_de_Presenca.Fisioterapia.model.Role;
import com.Lista_de_Presenca.Fisioterapia.model.Usuario;
import com.Lista_de_Presenca.Fisioterapia.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //Injeção por construtor
    public UsuarioService(UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder, JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Cadastrar
    public Usuario cadastrar(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        if (usuario.getRole() == null) {
            usuario.setRole(Role.ROLE_USER);
        }

        usuario.setSenha(
            passwordEncoder.encode(usuario.getSenha())
        );

        return usuarioRepository.save(usuario);
    }


    //Logar
    public Usuario login( String email, String senha){

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return usuario;
    } 
    
}


