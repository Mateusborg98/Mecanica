package br.com.techchallenge.mecanica.application.dto.estoque;

import java.util.UUID;

public record MovimentarEstoqueInput(UUID pecaId, int quantidade) {
}
