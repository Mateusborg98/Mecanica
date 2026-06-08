package br.com.techchallenge.mecanica.domain.ordemdeservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServico {
    private UUID id;
    private UUID clienteId;
    private UUID veiculoId;
    private UUID operadorId;
    private StatusOrdemDeServicoEnum status;
    private LocalDateTime dtInicioOs;
    private LocalDateTime dtFimOs;
    private BigDecimal valorTotalOs;
    private List<PecaOrdemDeServico> pecas;
    private List<ServicoOrdemDeServico> servicos;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrdemDeServicoJpaEntity other))
            return false;
        return id != null && id.equals(other.getId());
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

        for (ServicoOrdemDeServico ServicoOrdemDeServico : servicos) {
            total = total.add(ServicoOrdemDeServico.getServico().getPreco());
        }

        for (PecaOrdemDeServico PecaOrdemDeServico : pecas) {
            BigDecimal valorItem = PecaOrdemDeServico.getPeca().getPreco()
                    .multiply(BigDecimal.valueOf(PecaOrdemDeServico.getQuantidade()));
            total = total.add(valorItem);
        }
        return total;
    }
}
