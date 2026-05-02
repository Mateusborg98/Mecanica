package br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateItemOrdemDeServicoRequestDto {

    @NotNull
    private UUID pecaId;

    @NotNull
    @Positive
    private Integer quantidade;

    @NotNull
    @Positive
    private BigDecimal valorUnitario;

    public CreateItemOrdemDeServicoRequestDto(@NotNull UUID pecaId, @NotNull @Positive Integer quantidade,
            @NotNull @Positive BigDecimal valorUnitario) {
        this.pecaId = pecaId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public UUID getPecaId() {
        return pecaId;
    }

    public void setPecaId(UUID pecaId) {
        this.pecaId = pecaId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

}