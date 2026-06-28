package br.com.techchallenge.mecanica.domain.ordemdeservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrdemDeServico {

    private UUID id;
    private UUID clienteId;
    private UUID veiculoId;
    private UUID operadorId;

    private StatusOrdemDeServicoEnum status;

    private LocalDateTime dtInicioOs;
    private LocalDateTime dtFimOs;

    private BigDecimal valorTotalOs;

    private List<PecaOrdemDeServico> pecas = new ArrayList<>();
    private List<ServicoOrdemDeServico> servicos = new ArrayList<>();

    public OrdemDeServico(UUID id, UUID clienteId, UUID veiculoId, UUID operadorId, StatusOrdemDeServicoEnum status,
            LocalDateTime dtInicioOs, LocalDateTime dtFimOs, BigDecimal valorTotalOs, List<PecaOrdemDeServico> pecas,
            List<ServicoOrdemDeServico> servicos) {

        validarIdentificador(clienteId, "Cliente");
        validarIdentificador(veiculoId, "Veículo");
        validarIdentificador(operadorId, "Operador");

        this.id = id;
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.operadorId = operadorId;
        this.status = status;
        this.dtInicioOs = dtInicioOs;
        this.dtFimOs = dtFimOs;
        this.valorTotalOs = valorTotalOs;
        this.pecas = pecas;
        this.servicos = servicos;
    }

    public OrdemDeServico(UUID clienteId, UUID veiculoId, UUID operadorId) {

        validarIdentificador(clienteId, "Cliente");
        validarIdentificador(veiculoId, "Veículo");
        validarIdentificador(operadorId, "Operador");

        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.operadorId = operadorId;
    }

    public void atualizarOrdemDeServico(
            UUID clienteId,
            UUID veiculoId,
            UUID operadorId) {

        validarIdentificador(clienteId, "Cliente");
        validarIdentificador(veiculoId, "Veículo");
        validarIdentificador(operadorId, "Operador");

        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.operadorId = operadorId;

        this.status = StatusOrdemDeServicoEnum.RECEBIDA;
        this.valorTotalOs = BigDecimal.ZERO;
    }

    public void iniciarDiagnostico() {

        validarStatus(StatusOrdemDeServicoEnum.RECEBIDA);

        this.status = StatusOrdemDeServicoEnum.EM_DIAGNOSTICO;
    }

    public void aguardarAprovacao() {

        validarStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);

        this.status = StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO;
    }

    public void aprovarOrcamento(LocalDateTime dataHoraInicio) {

        validarStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

        this.status = StatusOrdemDeServicoEnum.EM_EXECUCAO;
        this.dtInicioOs = dataHoraInicio;
    }

    public void negarOrcamento() {

        validarStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

        this.status = StatusOrdemDeServicoEnum.EM_DIAGNOSTICO;
    }

    public void finalizar(LocalDateTime dataHoraFim) {

        validarStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);

        this.status = StatusOrdemDeServicoEnum.FINALIZADA;
        this.dtFimOs = dataHoraFim;
    }

    public void entregar() {

        validarStatus(StatusOrdemDeServicoEnum.FINALIZADA);

        this.status = StatusOrdemDeServicoEnum.ENTREGUE;
    }

    public void adicionarServico(ServicoOrdemDeServico servico) {

        if (servico == null) {
            throw new RegraNegocioException(
                    "Serviço não pode ser nulo");
        }

        if (status == StatusOrdemDeServicoEnum.FINALIZADA
                || status == StatusOrdemDeServicoEnum.ENTREGUE) {

            throw new RegraNegocioException(
                    "Não é possível alterar uma OS finalizada");
        }

        servicos.add(servico);

        recalcularValorTotal();
    }

    public void adicionarPeca(PecaOrdemDeServico peca) {

        if (peca == null) {
            throw new RegraNegocioException(
                    "Peça não pode ser nula");
        }

        if (status == StatusOrdemDeServicoEnum.FINALIZADA
                || status == StatusOrdemDeServicoEnum.ENTREGUE) {

            throw new RegraNegocioException(
                    "Não é possível alterar uma OS finalizada");
        }

        pecas.add(peca);

        recalcularValorTotal();
    }

    public List<PecaOrdemDeServico> getPecas() {
        return Collections.unmodifiableList(pecas);
    }

    public List<ServicoOrdemDeServico> getServicos() {
        return Collections.unmodifiableList(servicos);
    }

    private void recalcularValorTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (ServicoOrdemDeServico servico : servicos) {

            total = total.add(
                    servico.getServico().getPreco());
        }

        for (PecaOrdemDeServico peca : pecas) {
            total = total.add(peca.calcularValorTotal());
        }

        this.valorTotalOs = total;
    }

    private void validarStatus(
            StatusOrdemDeServicoEnum statusEsperado) {

        if (this.status != statusEsperado) {

            throw new RegraNegocioException(
                    "Transição inválida. Status atual: "
                            + this.status);
        }
    }

    private void validarIdentificador(
            UUID id,
            String entidade) {

        if (id == null) {

            throw new RegraNegocioException(
                    entidade + " obrigatório");
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof OrdemDeServico other)) {
            return false;
        }

        return id != null
                && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}