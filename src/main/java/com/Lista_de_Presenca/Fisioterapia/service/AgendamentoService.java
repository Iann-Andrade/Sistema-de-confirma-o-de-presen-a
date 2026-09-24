package com.Lista_de_Presenca.Fisioterapia.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Lista_de_Presenca.Fisioterapia.controller.AgendamentoController;
import com.Lista_de_Presenca.Fisioterapia.model.Agendamento;
import com.Lista_de_Presenca.Fisioterapia.repository.AgendamentoRepository;
import com.Lista_de_Presenca.Fisioterapia.repository.PresencaRepository;

@Service
public class AgendamentoService {
    
        private final AgendamentoRepository agendamentoRepository;
        private final PresencaRepository presencaRepository;
    
        public AgendamentoService(AgendamentoRepository agendamentoRepository, PresencaRepository presencaRepository){
            this.agendamentoRepository = agendamentoRepository;
            this.presencaRepository = presencaRepository;
        }
    
    
        //Roda toda Terça
        @Scheduled(cron = "0 0 0 * * TUE")
        public void GerarAgendamentoAutomatico() {

            LocalDate hoje = LocalDate.now();

            LocalDate proximaTerca = hoje.with(
                TemporalAdjusters.next(DayOfWeek.TUESDAY)
            );

            Agendamento agendamento = new Agendamento();

            agendamento.setData(proximaTerca);
            agendamento.setHoraInicio(LocalTime.of(19, 0));
            agendamento.setHoraFim(LocalTime.of(21, 0));
            agendamento.setNome("Atendimento de Fisioterapia Traumato-Ortopédica");
            agendamento.setDescricao("Foco: Avaliação e tratamento de dores articulares (ombros, quadril e tornozelos).");

            agendamentoRepository.save(agendamento);
        }
    
        //Criar novo agendamento manualmente
        public Agendamento criarAgendamento(Agendamento agendamento){
    
            System.out.println("Novo agendamento: " + agendamento);
    
            LocalDate dataSelect = agendamento.getData();
            LocalTime horaInicioSelect = agendamento.getHoraInicio();
            LocalTime horaFimSelect = agendamento.getHoraFim();

            List<Agendamento> agendamentosDoDia =
                agendamentoRepository.findByData(dataSelect);
    
                LocalDate hoje = LocalDate.now();

                if(dataSelect.isBefore(hoje)){

                    System.out.println("data de hoje: " + hoje);
                    System.out.println(dataSelect);
                    
                    throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Não é possível criar agendamentos anteríores ao dia atual.");
                };

                
                for (Agendamento existente : agendamentosDoDia) {

                    LocalTime inicioExistente = existente.getHoraInicio();
                    LocalTime fimExistente = existente.getHoraFim();

                
                    if (horaInicioSelect.isBefore(fimExistente)
                            && horaFimSelect.isAfter(inicioExistente)) {
                
                         throw new ResponseStatusException(
                HttpStatus.CONFLICT, 
                "Já existe um agendamento cadastrado para esta data e horário."
                    );
                };


            }   

                 return agendamentoRepository.save(agendamento);

          }


        //Buscar agendamentos
        public List<Agendamento> listarPorData(LocalDate data,  Integer agendamentoId, Integer usuarioId){

            List<Agendamento> agendamentos = agendamentoRepository.findByData(data);

            agendamentos.forEach(agendamento -> {
                boolean estaConfirmado = presencaRepository.existsByAgendamentoIdAndUsuarioId(agendamento.getId(), usuarioId);
                agendamento.setConfirmado(estaConfirmado);
            });

            return agendamentos;
        }
}
