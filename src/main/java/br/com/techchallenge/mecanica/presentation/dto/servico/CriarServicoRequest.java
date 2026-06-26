package br.com.techchallenge.mecanica.presentation.dto.servico;

import java.math.BigDecimal;

public record CriarServicoRequest(
        String descricao,
        BigDecimal preco) {
}