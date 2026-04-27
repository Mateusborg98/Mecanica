package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrdemDeServicoResumoDto {

    private UUID id;
    private String status;
    private LocalDateTime dtInicioOs;
    private String nomeCliente;
    private String placaVeiculo;

    public OrdemDeServicoResumoDto(
            UUID id,
            String status,
            LocalDateTime dtInicioOs,
            String nomeCliente,
            String placaVeiculo) {
        this.id = id;
        this.status = status;
        this.dtInicioOs = dtInicioOs;
        this.nomeCliente = nomeCliente;
        this.placaVeiculo = placaVeiculo;
    }

    public UUID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDtInicioOs() {
        return dtInicioOs;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

}
