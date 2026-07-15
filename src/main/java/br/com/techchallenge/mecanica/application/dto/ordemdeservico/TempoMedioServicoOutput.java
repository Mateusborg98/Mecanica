package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.UUID;

public record TempoMedioServicoOutput(UUID servicoId, long tempoMedioEmMinutos) {
}
