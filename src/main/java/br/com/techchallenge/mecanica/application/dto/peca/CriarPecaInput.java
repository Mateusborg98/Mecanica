package br.com.techchallenge.mecanica.application.dto.peca;

import java.math.BigDecimal;

public record CriarPecaInput(
        String nome,
        String marca,
        BigDecimal preco) {
}