package br.com.techchallenge.mecanica.domain.peca;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
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
public class Peca {

    private UUID id;
    private String nome;
    private String marca;
    private BigDecimal preco;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PecaJpaEntity other))
            return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

