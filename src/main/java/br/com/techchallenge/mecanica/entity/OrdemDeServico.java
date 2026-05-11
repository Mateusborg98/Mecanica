package br.com.techchallenge.mecanica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import jakarta.persistence.CascadeType;
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

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne
    private Operador operador;

    @Enumerated(EnumType.STRING)
    private StatusOrdemDeServicoEnum status;

    private LocalDateTime dtInicioOs;
    private LocalDateTime dtFimOs;

    private BigDecimal valorTotalOs;

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PecaOrdemDeServico> pecas = new ArrayList<>();

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicoOrdemDeServico> servicos = new ArrayList<>();

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

    public void adicionarServico(ServicoOrdemDeServico servico) {
        if (servico == null) {
            throw new RegraNegocioException("Serviço não pode ser nulo");
        }
        this.servicos.add(servico);
    }

    public void adicionarPeca(PecaOrdemDeServico peca) {
        if (peca == null) {
            throw new RegraNegocioException("Peça não pode ser nula");
        }
        this.pecas.add(peca);
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ServicoOrdemDeServico servicoOrdemDeServico : this.servicos) {
            total = total.add(servicoOrdemDeServico.getServico().getPreco());
        }

        for (PecaOrdemDeServico pecaOrdemDeServico : this.pecas) {
            BigDecimal valorItem = pecaOrdemDeServico.getPeca().getPreco()
                    .multiply(BigDecimal.valueOf(pecaOrdemDeServico.getQuantidade()));
            total = total.add(valorItem);
        }
        return total;
    }
}
