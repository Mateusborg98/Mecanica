package br.com.techchallenge.mecanica.application.dto.peca;

import java.math.BigDecimal;

public record AtualizarPecaInput(
        String nome,
        String marca,
        BigDecimal preco) {
}