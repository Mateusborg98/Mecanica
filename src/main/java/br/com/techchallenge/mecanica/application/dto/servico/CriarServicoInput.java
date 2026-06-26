package br.com.techchallenge.mecanica.application.dto.servico;

import java.math.BigDecimal;

public record CriarServicoInput(
        String descricao,
        BigDecimal preco) {
}