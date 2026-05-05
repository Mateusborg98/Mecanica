package br.com.techchallenge.mecanica.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Servico {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @ManyToOne(optional = false)
    private OrdemDeServico ordemDeServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusServicoEnum status;

    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Servico other))
            return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Servico(UUID id, String descricao, BigDecimal preco, OrdemDeServico ordemDeServico, StatusServicoEnum status,
            LocalDateTime dtInicio) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.ordemDeServico = ordemDeServico;
        this.status = status;
        this.dtInicio = dtInicio;
    }

}
