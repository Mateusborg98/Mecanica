package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.UUID;

public record AdicionarServicoOrdemDeServicoInput(
        UUID ordemDeServicoId,
        UUID servicoId) {
}