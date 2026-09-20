package com.Lista_de_Presenca.Fisioterapia.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Lista_de_Presenca.Fisioterapia.model.Agendamento;



@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer>{
    
    List<Agendamento> findByData(LocalDate data);

}
