package br.com.techchallenge.mecanica.presentation.dto.servico;

import java.math.BigDecimal;

public record AtualizarServicoRequest(
        String descricao,
        BigDecimal preco) {
}
