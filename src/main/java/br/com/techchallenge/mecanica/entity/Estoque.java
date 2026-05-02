package br.com.techchallenge.mecanica.entity;

import java.util.UUID;

import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    private Peca peca;

    @Column(nullable = false)
    private Integer quantidade;

    public void registrarEntrada(int qtd) {
        if (qtd <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }
        this.quantidade += qtd;
    }

    public void registrarSaida(int qtd) {
        if (qtd <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }
        if (qtd > this.quantidade) {
            throw new RegraNegocioException("Estoque insuficiente");
        }
        this.quantidade -= qtd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Estoque other))
            return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
