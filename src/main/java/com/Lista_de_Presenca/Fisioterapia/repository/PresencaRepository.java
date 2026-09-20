package com.Lista_de_Presenca.Fisioterapia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Lista_de_Presenca.Fisioterapia.model.Presenca;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Integer>{
   
    //Método para verificar se o usuario já confirmou presença
    boolean existsByAgendamentoIdAndUsuarioId(
        Integer agendamentoId,
        Integer usuarioId
    );

    List<Presenca> findByAgendamentoId(Integer agendamentoId);
}
