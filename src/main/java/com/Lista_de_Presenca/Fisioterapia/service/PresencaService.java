package com.Lista_de_Presenca.Fisioterapia.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.Lista_de_Presenca.Fisioterapia.model.Agendamento;
import com.Lista_de_Presenca.Fisioterapia.model.Presenca;
import com.Lista_de_Presenca.Fisioterapia.model.Usuario;
import com.Lista_de_Presenca.Fisioterapia.repository.AgendamentoRepository;
import com.Lista_de_Presenca.Fisioterapia.repository.PresencaRepository;
import com.Lista_de_Presenca.Fisioterapia.repository.UsuarioRepository;

@Service
public class PresencaService {
    
    private final PresencaRepository presencaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    
        public PresencaService(PresencaRepository presencaRepository, AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository){
            this.presencaRepository = presencaRepository;
            this.agendamentoRepository = agendamentoRepository;
            this.usuarioRepository = usuarioRepository;
        }


    public Presenca confirmarPresenca(Integer agendamentoId, Integer usuarioId) {
    
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        boolean jaConfirmou = presencaRepository.existsByAgendamentoIdAndUsuarioId(agendamentoId, usuarioId);

        if(jaConfirmou){
            throw new RuntimeException("Usuário já confirmou presença.");                                      
        }

        Presenca presenca = new Presenca();
        presenca.setAgendamento(agendamento);
        presenca.setUsuario(usuario);
        presenca.setConfirmacaoHoraData(LocalDateTime.now());

        
        return presencaRepository.save(presenca);
    }

    public List<String> listaConfirmados(Integer agendamentoId){

        List<Presenca> presencas = presencaRepository.findByAgendamentoId(agendamentoId);

        List<String> nomes = new ArrayList<>();

        for(Presenca presenca : presencas){

            nomes.add(presenca.getUsuario().getNome());

        }

        return nomes;
    }

}
