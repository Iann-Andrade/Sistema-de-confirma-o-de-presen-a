package com.Lista_de_Presenca.Fisioterapia.dto;

import java.time.LocalDate;

public class CriarAgendamentoDTO {
    
  private LocalDate dataSelecionada;

    public LocalDate getDataSelecionada() {
        return dataSelecionada;
    }

    public void setDataSelecionada(LocalDate dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

}
