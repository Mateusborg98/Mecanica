package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.List;
import java.util.UUID;

public record CriarOrdemDeServicoInput(
        UUID clienteId,
        UUID veiculoId,
        UUID operadorId,
        List<UUID> servicoIds,
        List<PecaInput> pecas) {

    public CriarOrdemDeServicoInput(UUID clienteId, UUID veiculoId, UUID operadorId) {
        this(clienteId, veiculoId, operadorId, List.of(), List.of());
    }

    public record PecaInput(UUID pecaId, int quantidade) {
    }
}
