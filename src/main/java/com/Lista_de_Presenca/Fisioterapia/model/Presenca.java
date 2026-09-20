package com.Lista_de_Presenca.Fisioterapia.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "presenca")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime confirmacaoHoraData;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Agendamento getAgendamento(){
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento){
        this.agendamento = agendamento;
    }

    public LocalDateTime getConfirmacaoHoraData(){
        return confirmacaoHoraData;
    }

    public void setConfirmacaoHoraData(LocalDateTime confirmacaoHoraData){
        this.confirmacaoHoraData = confirmacaoHoraData;
    }


                                           
}
