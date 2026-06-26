package br.com.techchallenge.mecanica.domain.pecaordemdeservico;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PecaOrdemDeServico {

    private UUID id;
    private Peca peca;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    public PecaOrdemDeServico(UUID id, Peca peca, Integer quantidade, BigDecimal valorUnitario) {

        if (peca == null) {
            throw new RegraNegocioException(
                    "Peça obrigatória");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException(
                    "Quantidade inválida");
        }

        if (valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) == 0) {
            throw new RegraNegocioException(
                    "Valor unitário invalido");
        }

        this.id = id;
        this.peca = peca;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public void atualizarPecaOrdemDeServico(UUID id, Peca peca, Integer quantidade, BigDecimal valorUnitario) {

        if (peca == null) {
            throw new RegraNegocioException(
                    "Peça obrigatória");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException(
                    "Quantidade inválida");
        }

        if (valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) == 0) {
            throw new RegraNegocioException(
                    "Valor unitário invalido");
        }

        this.id = id;
        this.peca = peca;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal calcularValorTotal() {
        return valorUnitario.multiply(
                BigDecimal.valueOf(quantidade));
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof PecaOrdemDeServico other)) {
            return false;
        }

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}