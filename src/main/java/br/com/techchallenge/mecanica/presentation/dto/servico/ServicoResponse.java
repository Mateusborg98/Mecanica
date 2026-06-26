package br.com.techchallenge.mecanica.presentation.dto.servico;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoResponse(
        UUID id,
        String descricao,
        BigDecimal preco,
        boolean ativo
) {}