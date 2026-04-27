package br.com.techchallenge.mecanica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class OrdemDeServico {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdemDeServicoEnum status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dtInicioOs;

    private LocalDateTime dtFimOs;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Veiculo veiculo;

    @ManyToOne(optional = false)
    private Operador operador;

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.PERSIST)
    private List<ItemOrdemDeServico> itens = new ArrayList<>();

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.PERSIST)
    private List<Servico> servicos = new ArrayList<>();

    public OrdemDeServico() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrdemDeServico other))
            return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public UUID getId() {
        return id;
    }

    public StatusOrdemDeServicoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemDeServicoEnum status) {
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

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public List<ItemOrdemDeServico> getItens() {
        return itens;
    }

    public void adicionarItem(ItemOrdemDeServico item) {
        itens.add(item);
        item.setOrdemDeServico(this);
    }

    public void removerItem(ItemOrdemDeServico item) {
        itens.remove(item);
        item.setOrdemDeServico(null);
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

    public void adicionarServico(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não pode ser nulo");
        }

        this.servicos.add(servico);
        calcularValorTotal();
    }

    public void adicionarPeca(Peca peca, Integer quantidade) {
        if (peca == null) {
            throw new IllegalArgumentException("Peça não pode ser nula");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        ItemOrdemDeServico item = new ItemOrdemDeServico();
        item.setOrdemDeServico(this);
        item.setPeca(peca);
        item.setQuantidade(quantidade);
        item.setValorUnitario(peca.getPreco());

        this.itens.add(item);
        calcularValorTotal();
    }

    public void calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Servico servico : servicos) {
            total = total.add(servico.getPreco());
        }

        for (ItemOrdemDeServico item : itens) {
            BigDecimal valorItem = item.getValorUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(valorItem);
        }
    }

}
