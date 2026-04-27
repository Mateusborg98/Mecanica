package br.com.techchallenge.mecanica.dto.veiculoDto;

import java.util.UUID;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResumoDto;

public class VeiculoResponseDto {

    private UUID id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private ClienteResumoDto cliente;

    public VeiculoResponseDto(
            UUID id,
            String placa,
            String marca,
            String modelo,
            Integer ano,
            ClienteResumoDto cliente) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cliente = cliente;
    }

    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public ClienteResumoDto getCliente() {
        return cliente;
    }

}