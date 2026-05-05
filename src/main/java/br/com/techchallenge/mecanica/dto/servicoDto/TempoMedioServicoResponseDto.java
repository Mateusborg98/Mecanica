package br.com.techchallenge.mecanica.dto.servicoDto;

import java.time.Duration;

public class TempoMedioServicoResponseDto {

    private String servico;
    private long tempoMedioEmMinutos;

    public TempoMedioServicoResponseDto(String servico, Duration duration) {
        this.servico = servico;
        this.tempoMedioEmMinutos = duration.toMinutes();
    }

    public String getServico() {
        return servico;
    }

    public long getTempoMedioEmMinutos() {
        return tempoMedioEmMinutos;
    }
}
