package com.Lista_de_Presenca.Fisioterapia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

import com.Lista_de_Presenca.Fisioterapia.model.Usuario;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;
import com.Lista_de_Presenca.Fisioterapia.service.PresencaService;


@Controller
@RequestMapping("/presenca")
@CrossOrigin(origins = "*")
public class PresencaController {
    
    private final PresencaService presencaService;
    private final JwtService jwtService;

    public PresencaController(PresencaService presencaService, JwtService jwtService){
        this.presencaService = presencaService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{agendamentoId}/confirmar")
    public ResponseEntity<?> confirmarPresenca(@PathVariable Integer agendamentoId, @AuthenticationPrincipal Usuario usuarioLogado){

        Integer usuarioId = usuarioLogado.getId();
        presencaService.confirmarPresenca(agendamentoId, usuarioId);
        
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{agendamentoId}/confirmados")
    public ResponseEntity<?> listaConfirmados(@PathVariable Integer agendamentoId){

        List<String> nomes = presencaService.listaConfirmados(agendamentoId);

        return ResponseEntity.ok(nomes);
    }



}
