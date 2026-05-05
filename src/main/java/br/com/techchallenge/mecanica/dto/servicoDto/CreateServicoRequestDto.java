package br.com.techchallenge.mecanica.dto.servicoDto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateServicoRequestDto {

    @NotBlank
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal preco;

    private UUID ordemId;

    public CreateServicoRequestDto(@NotBlank String descricao, @NotNull @Positive BigDecimal preco, UUID ordemId) {
        this.descricao = descricao;
        this.preco = preco;
        this.ordemId = ordemId;
    }

    public CreateServicoRequestDto() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public UUID getOrdemId() {
        return ordemId;
    }

    public void setOrdemId(UUID ordemId) {
        this.ordemId = ordemId;
    }

}