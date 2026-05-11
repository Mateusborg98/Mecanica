package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRequestDto {

    private UUID servicoId;

}
