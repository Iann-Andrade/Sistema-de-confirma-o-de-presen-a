package com.Lista_de_Presenca.Fisioterapia.repository;

import com.Lista_de_Presenca.Fisioterapia.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByEmail(String email);

    Optional<Usuario> findById(Long usuarioId);

    //Adiciona o metofo findByEmail
    Optional<Usuario> findByEmail(String email);

}
