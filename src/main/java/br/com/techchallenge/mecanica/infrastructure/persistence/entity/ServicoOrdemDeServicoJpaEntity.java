package br.com.techchallenge.mecanica.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicoOrdemDeServicoJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemDeServicoJpaEntity ordemDeServicoJpaEntity;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoJpaEntity servicoJpaEntity;

    @Enumerated(EnumType.STRING)
    private StatusServicoEnum status;

    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServicoJpaEntity other))
            return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}