package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddServicoPecaOrdemDeServicoDto {

    private List<PecaRequestDto> pecas;
    private List<ServicoRequestDto> servicos;

}
