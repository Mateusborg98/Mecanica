package br.com.techchallenge.mecanica.dto.pecaDto;

import java.math.BigDecimal;
import java.util.UUID;

public class PecaResumoDto {

    private UUID id;
    private String nome;
    private String marca;
    private BigDecimal preco;

    public PecaResumoDto(UUID id, String nome, String marca, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMarca() {
        return marca;
    }

    public BigDecimal getPreco() {
        return preco;
    }

}