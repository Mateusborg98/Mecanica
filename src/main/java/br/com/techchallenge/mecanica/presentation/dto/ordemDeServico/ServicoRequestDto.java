package br.com.techchallenge.mecanica.presentation.dto.ordemDeServico;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoRequestDto {

    @NotNull
    private UUID servicoId;

}
