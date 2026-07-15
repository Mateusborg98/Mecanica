package br.com.techchallenge.mecanica.application.dto.servico;

import java.math.BigDecimal;

public record AtualizarServicoInput(
        String descricao,
        BigDecimal preco) {
}