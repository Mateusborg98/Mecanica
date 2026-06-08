package br.com.techchallenge.mecanica.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
}