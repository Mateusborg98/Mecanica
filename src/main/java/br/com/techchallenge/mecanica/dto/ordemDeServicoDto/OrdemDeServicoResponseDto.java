package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.operadorDto.OperadorResumoDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.ItemOrdemDeServico;
import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;

public class OrdemDeServicoResponseDto {

    private UUID id;
    private Enum<StatusOrdemDeServicoEnum> status;
    private LocalDateTime dtInicioOs;
    private LocalDateTime dtFimOs;

    private Cliente cliente;
    private Veiculo veiculo;
    private OperadorResumoDto operador;

    private List<ItemOrdemDeServico> itens;
    private List<Servico> servicos;

    public OrdemDeServicoResponseDto(
            UUID id,
            StatusOrdemDeServicoEnum status,
            LocalDateTime dtInicioOs,
            LocalDateTime dtFimOs,
            Cliente cliente,
            Veiculo veiculo,
            OperadorResumoDto operador,
            List<ItemOrdemDeServico> itens,
            List<Servico> servicos) {
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

    public void setId(UUID id) {
        this.id = id;
    }

    public Enum<StatusOrdemDeServicoEnum> getStatus() {
        return status;
    }

    public void setStatus(Enum<StatusOrdemDeServicoEnum> status) {
        this.status = status;
    }

    public LocalDateTime getDtInicioOs() {
        return dtInicioOs;
    }

    public void setDtInicioOs(LocalDateTime dtInicioOs) {
        this.dtInicioOs = dtInicioOs;
    }

    public LocalDateTime getDtFimOs() {
        return dtFimOs;
    }

    public void setDtFimOs(LocalDateTime dtFimOs) {
        this.dtFimOs = dtFimOs;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public OperadorResumoDto getOperador() {
        return operador;
    }

    public void setOperador(OperadorResumoDto operador) {
        this.operador = operador;
    }

    public List<ItemOrdemDeServico> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrdemDeServico> itens) {
        this.itens = itens;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

}