package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TempoMedioServicoResponseDto {

    private String servico;
    private Long tempoMedioEmMinutos;

}
