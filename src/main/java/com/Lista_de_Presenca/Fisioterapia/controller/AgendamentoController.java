package com.Lista_de_Presenca.Fisioterapia.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.Lista_de_Presenca.Fisioterapia.service.AgendamentoService;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;
import com.Lista_de_Presenca.Fisioterapia.service.UsuarioService;
import org.springframework.web.bind.annotation.RequestBody;
import com.Lista_de_Presenca.Fisioterapia.dto.CriarAgendamentoDTO;
import com.Lista_de_Presenca.Fisioterapia.model.Agendamento;
import com.Lista_de_Presenca.Fisioterapia.model.Usuario;

@RestController
@RequestMapping("/agendamento")
@CrossOrigin(origins = "*")
public class AgendamentoController {
    
    private final AgendamentoService agendamentoService;
    private final JwtService jwtService;

    public AgendamentoController(AgendamentoService agendamentoService, JwtService jwtService){
        this.agendamentoService = agendamentoService;
        this.jwtService = jwtService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarAgendamento(@RequestBody Agendamento agendamento){

        try{

            Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamento);
            
            System.out.println("Data recebida: " + agendamento);
    
            return ResponseEntity.ok(novoAgendamento);

        }catch(ResponseStatusException e){

            return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.of("message", e.getReason()));
        }

    }

    @GetMapping
    public ResponseEntity<?> buscarAgendamentos(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, @AuthenticationPrincipal Usuario usuarioLogado, Integer agendamentoId){

            if (data != null && data.isBefore(LocalDate.now())) {
                // Retorna HTTP 400 (Bad Request) com uma mensagem de texto simples
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Não é possível pesquisar agendamentos em datas passadas.");
            };

            Integer usuarioId = usuarioLogado.getId();

            List<Agendamento> agendamentos = agendamentoService.listarPorData( data, agendamentoId, usuarioId);

            return ResponseEntity.ok(agendamentos);
        }

}
