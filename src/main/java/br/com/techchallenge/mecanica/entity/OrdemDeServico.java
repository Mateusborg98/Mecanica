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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServico {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdemDeServicoEnum status;

    @Column(nullable = false)
    private LocalDateTime dtInicioOs;

    private LocalDateTime dtFimOs;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Veiculo veiculo;

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.PERSIST)
    private List<ItemOrdemDeServico> itens = new ArrayList<>();

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.PERSIST)
    private List<Servico> servicos = new ArrayList<>();

    public void adicionarPeca(Peca peca, int quantidade) {
        if (peca == null) {
            throw new IllegalArgumentException("Peça não pode ser nula");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        ItemOrdemDeServico item = new ItemOrdemDeServico();
        item.setOrdemDeServico(this);
        item.setPeca(peca);
        item.setQuantidade(quantidade);
        item.setValorUnitario(peca.getPreco());

        itens.add(item);
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

    public void adicionarServico(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não pode ser nulo");
        }

        this.servicos.add(servico);
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
