package br.com.techchallenge.mecanica.domain.servicoordemdeservico;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicoOrdemDeServico {
    private UUID id;
    private Servico servico;
    private StatusServicoEnum status;
    private LocalDateTime dtInicio;
    private LocalDateTime dtFim;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Servico other))
            return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}