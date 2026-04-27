package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResumoDto;
import br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto.ItemOrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.operadorDto.OperadorResumoDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResumoDto;

public class OrdemDeServicoResponseDto {

    private UUID id;
    private String status;
    private LocalDateTime dtInicioOs;
    private LocalDateTime dtFimOs;

    private ClienteResumoDto cliente;
    private VeiculoResumoDto veiculo;
    private OperadorResumoDto operador;

    private List<ItemOrdemDeServicoResponseDto> itens;
    private List<ServicoResponseDto> servicos;

    public OrdemDeServicoResponseDto(
            UUID id,
            String status,
            LocalDateTime dtInicioOs,
            LocalDateTime dtFimOs,
            ClienteResumoDto cliente,
            VeiculoResumoDto veiculo,
            OperadorResumoDto operador,
            List<ItemOrdemDeServicoResponseDto> itens,
            List<ServicoResponseDto> servicos) {
        this.id = id;
        this.status = status;
        this.dtInicioOs = dtInicioOs;
        this.dtFimOs = dtFimOs;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.operador = operador;
        this.itens = itens;
        this.servicos = servicos;
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

    public LocalDateTime getDtFimOs() {
        return dtFimOs;
    }

    public ClienteResumoDto getCliente() {
        return cliente;
    }

    public VeiculoResumoDto getVeiculo() {
        return veiculo;
    }

    public OperadorResumoDto getOperador() {
        return operador;
    }

    public List<ItemOrdemDeServicoResponseDto> getItens() {
        return itens;
    }

    public List<ServicoResponseDto> getServicos() {
        return servicos;
    }

}