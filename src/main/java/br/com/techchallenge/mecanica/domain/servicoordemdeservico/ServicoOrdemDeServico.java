package br.com.techchallenge.mecanica.domain.servicoordemdeservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServicoOrdemDeServico {

    private UUID id;
    private Servico servico;
    private StatusServicoEnum status;
    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;
    private BigDecimal valorCobrado;

    public ServicoOrdemDeServico(UUID id, Servico servico, BigDecimal valorCobrado) {
        this.id = id;
        this.servico = servico;
        this.valorCobrado = valorCobrado;
    }

    public ServicoOrdemDeServico(Servico servico) {

        if (servico == null) {
            throw new RegraNegocioException(
                    "Serviço obrigatório");
        }

        this.servico = servico;
        this.status = StatusServicoEnum.AGUARDANDO;
    }

    public void iniciar(LocalDateTime dataHora) {

        if (status != StatusServicoEnum.AGUARDANDO) {
            throw new RegraNegocioException(
                    "Serviço não pode ser iniciado");
        }

        this.status = StatusServicoEnum.EM_EXECUCAO;
        this.dtInicio = dataHora;
    }

    public void finalizar(LocalDateTime dataHora) {

        if (status != StatusServicoEnum.EM_EXECUCAO) {
            throw new RegraNegocioException(
                    "Serviço não pode ser finalizado");
        }

        this.status = StatusServicoEnum.FINALIZADO;
        this.dtFim = dataHora;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof ServicoOrdemDeServico other)) {
            return false;
        }

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}