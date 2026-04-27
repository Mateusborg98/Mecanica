package br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemOrdemDeServicoResponseDto {

    private UUID id;
    private String descricao;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public ItemOrdemDeServicoResponseDto(
            UUID id,
            String descricao,
            Integer quantidade,
            BigDecimal valorUnitario,
            BigDecimal valorTotal) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

}
