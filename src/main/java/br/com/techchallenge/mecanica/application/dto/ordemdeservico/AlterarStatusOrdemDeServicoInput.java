package br.com.techchallenge.mecanica.application.dto.ordemdeservico;

import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;

public record AlterarStatusOrdemDeServicoInput(
        UUID ordemDeServicoId,
        StatusOrdemDeServicoEnum novoStatus) {
}