package br.com.techchallenge.mecanica.domain.ordemdeservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder.Default
    private List<PecaOrdemDeServico> pecas = new ArrayList<>();

    @Builder.Default
    private List<ServicoOrdemDeServico> servicos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrdemDeServico other))
            return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void iniciarDiagnostico() {
        validarStatus(StatusOrdemDeServicoEnum.RECEBIDA);
        this.status = StatusOrdemDeServicoEnum.EM_DIAGNOSTICO;
    }

    public void aprovarOrcamento(LocalDateTime localDateTime) {
        validarStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        this.status = StatusOrdemDeServicoEnum.EM_EXECUCAO;
        this.dtInicioOs = localDateTime;
    }

    public void negarOrcamento() {
        validarStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        this.status = StatusOrdemDeServicoEnum.EM_DIAGNOSTICO;
    }

    public void finalizar(LocalDateTime localDateTime) {
        validarStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);
        this.status = StatusOrdemDeServicoEnum.FINALIZADA;
        this.dtFimOs = localDateTime;
    }

    public void entregar() {
        validarStatus(StatusOrdemDeServicoEnum.FINALIZADA);
        this.status = StatusOrdemDeServicoEnum.ENTREGUE;
    }

    private void validarStatus(StatusOrdemDeServicoEnum statusEsperado) {
        if (this.getStatus() != statusEsperado) {
            throw new RegraNegocioException(
                    "Transição inválida. Status atual: " + this.getStatus());
        }
    }

    public void adicionarServico(ServicoOrdemDeServico servico) {
        if (servico == null) {
            throw new RegraNegocioException("Serviço não pode ser nulo");
        }
        this.servicos.add(servico);
        calcularValorTotal();
    }

    public void adicionarPeca(PecaOrdemDeServico peca) {
        if (peca == null) {
            throw new RegraNegocioException("Peça não pode ser nula");
        }
        this.pecas.add(peca);
        calcularValorTotal();
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ServicoOrdemDeServico servico : servicos) {
            total = total.add(servico.getServico().getPreco());
        }

        for (PecaOrdemDeServico peca : pecas) {
            BigDecimal valorItem = peca.getPeca().getPreco()
                    .multiply(BigDecimal.valueOf(peca.getQuantidade()));
            total = total.add(valorItem);
        }

        valorTotalOs = total;
        return total;
    }
}
