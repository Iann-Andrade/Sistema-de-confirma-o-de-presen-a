package com.Lista_de_Presenca.Fisioterapia.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Lista_de_Presenca.Fisioterapia.model.Agendamento;
import com.Lista_de_Presenca.Fisioterapia.model.Usuario;



@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer>{
    
    List<Agendamento> findByData(LocalDate data);

    Optional<Agendamento> findFirstByDataGreaterThanEqualOrderByDataAscHoraInicioAsc(LocalDate data);

    List<Agendamento> findAllByOrderByDataAscHoraInicioAsc(LocalDate data);

    List<Agendamento> findByDataGreaterThan(LocalDate data);

}
