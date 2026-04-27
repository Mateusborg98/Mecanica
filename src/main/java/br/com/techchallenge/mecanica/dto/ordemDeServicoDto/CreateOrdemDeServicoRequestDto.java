package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.entity.ItemOrdemDeServico;
import br.com.techchallenge.mecanica.entity.Servico;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateOrdemDeServicoRequestDto {

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID veiculoId;

    @NotNull
    private UUID operadorId;

    @NotEmpty
    private List<ItemOrdemDeServico> itens;

    private List<Servico> servicos;

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public UUID getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(UUID veiculoId) {
        this.veiculoId = veiculoId;
    }

    public UUID getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(UUID operadorId) {
        this.operadorId = operadorId;
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