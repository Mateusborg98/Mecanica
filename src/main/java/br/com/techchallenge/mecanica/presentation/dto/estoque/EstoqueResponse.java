package br.com.techchallenge.mecanica.presentation.dto.estoque;

import java.util.UUID;

public record EstoqueResponse(UUID id, UUID pecaId, int quantidade) {
}
