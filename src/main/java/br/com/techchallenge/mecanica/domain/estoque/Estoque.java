package br.com.techchallenge.mecanica.domain.estoque;

import java.util.UUID;

import br.com.techchallenge.mecanica.domain.exception.QuantidadeEstoqueException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Estoque {

    private UUID id;
    private UUID pecaId;
    private Integer quantidade;
    private Long versao;

    public Estoque(UUID pecaId, Integer quantidade) {

        this(null, pecaId, quantidade, null);
    }

    public Estoque(UUID id, UUID pecaId, Integer quantidade) {

        this(id, pecaId, quantidade, null);
    }

    public Estoque(UUID id, UUID pecaId, Integer quantidade, Long versao) {

        if (pecaId == null) {
            throw new QuantidadeEstoqueException("Peça obrigatória");
        }

        if (quantidade == null || quantidade < 0) {
            throw new QuantidadeEstoqueException(
                    "Quantidade inicial inválida");
        }

        this.id = id;
        this.pecaId = pecaId;
        this.quantidade = quantidade;
        this.versao = versao;
    }

    public void registrarEntrada(int qtd) {

        if (qtd <= 0) {
            throw new QuantidadeEstoqueException(
                    "Quantidade inválida");
        }

        this.quantidade += qtd;
    }

    public void registrarSaida(int qtd) {

        if (qtd <= 0) {
            throw new QuantidadeEstoqueException(
                    "Quantidade inválida");
        }

        if (qtd > this.quantidade) {
            throw new QuantidadeEstoqueException(
                    "Estoque insuficiente");
        }

        this.quantidade -= qtd;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Estoque other)) {
            return false;
        }

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
