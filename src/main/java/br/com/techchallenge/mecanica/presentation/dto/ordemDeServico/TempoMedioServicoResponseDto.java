package br.com.techchallenge.mecanica.presentation.dto.ordemDeServico;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TempoMedioServicoResponseDto {

    private UUID servicoId;
    private Long tempoMedioEmMinutos;

}
