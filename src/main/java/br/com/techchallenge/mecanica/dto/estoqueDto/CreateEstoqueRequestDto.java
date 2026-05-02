package br.com.techchallenge.mecanica.dto.estoqueDto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateEstoqueRequestDto {

    @NotNull
    private UUID pecaId;

    @NotNull
    @Positive
    private Integer quantidade;

    public CreateEstoqueRequestDto(UUID pecaId, Integer quantidade) {
        this.pecaId = pecaId;
        this.quantidade = quantidade;
    }

    public UUID getPecaId() {
        return pecaId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}