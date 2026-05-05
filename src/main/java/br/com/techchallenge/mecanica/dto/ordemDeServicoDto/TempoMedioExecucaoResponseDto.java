package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.time.Duration;

public class TempoMedioExecucaoResponseDto {

    private long tempoMedioEmMinutos;

    public TempoMedioExecucaoResponseDto(Duration duration) {
        this.tempoMedioEmMinutos = duration.toMinutes();
    }

    public long getTempoMedioEmMinutos() {
        return tempoMedioEmMinutos;
    }
}