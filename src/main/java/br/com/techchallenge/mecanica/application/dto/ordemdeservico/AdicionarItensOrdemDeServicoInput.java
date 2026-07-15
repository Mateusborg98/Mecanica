package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.List;
import java.util.UUID;

public record AdicionarItensOrdemDeServicoInput(
        UUID ordemDeServicoId, List<UUID> servicoIds, List<PecaInput> pecas) {
    public record PecaInput(UUID pecaId, int quantidade) {
    }
}
