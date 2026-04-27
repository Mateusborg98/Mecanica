package br.com.techchallenge.mecanica.dto.veiculoDto;

import java.util.UUID;

public class VeiculoResumoDto {

    private UUID id;
    private String placa;
    private String modelo;

    public VeiculoResumoDto(UUID id, String placa, String modelo) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
    }

    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    // getters
}