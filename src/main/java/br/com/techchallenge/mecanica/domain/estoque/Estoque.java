package br.com.techchallenge.mecanica.domain.estoque;

import java.util.UUID;

import br.com.techchallenge.mecanica.domain.exception.QuantidadeEstoqueException;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;
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
public class Estoque {

    private UUID id;
    private UUID pecaId;
    private Integer quantidade;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EstoqueJpaEntity other))
            return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void registrarEntrada(int qtd) {
        if (qtd <= 0) {
            throw new QuantidadeEstoqueException("Quantidade inválida");
        }
        this.quantidade += qtd;
    }

    public void registrarSaida(int qtd) {
        if (qtd <= 0) {
            throw new QuantidadeEstoqueException("Quantidade inválida");
        } else if (qtd > this.quantidade) {
            throw new QuantidadeEstoqueException("Estoque insuficiente");
        }
        this.quantidade -= qtd;
    }
}

