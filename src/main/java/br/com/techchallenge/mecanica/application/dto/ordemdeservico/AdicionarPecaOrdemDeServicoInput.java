package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.UUID;

public record AdicionarPecaOrdemDeServicoInput(
        UUID pecaOrdemDeServicoId,
        UUID ordemDeServicoId,
        UUID pecaId,
        Integer quantidade) {
}