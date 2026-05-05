package br.com.techchallenge.mecanica.dto.servicoDto;

import java.math.BigDecimal;
import java.util.UUID;

public class ServicoResponseDto {

    private UUID id;
    private String descricao;
    private BigDecimal preco;

    public ServicoResponseDto(UUID id, String descricao, BigDecimal preco) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
    }

    public ServicoResponseDto() {
        //TODO Auto-generated constructor stub
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

}