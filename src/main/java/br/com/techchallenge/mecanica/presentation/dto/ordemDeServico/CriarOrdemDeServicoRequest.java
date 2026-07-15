package br.com.techchallenge.mecanica.presentation.dto.ordemDeServico;

import java.util.List;

import br.com.techchallenge.mecanica.presentation.annotation.CpfCnpjAnnotation;
import br.com.techchallenge.mecanica.presentation.annotation.PlacaValida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record CriarOrdemDeServicoRequest(
    @NotBlank
    @CpfCnpjAnnotation
    String cpfCnpj,

    @NotBlank
    @PlacaValida
    String placa,

    List<@Valid ServicoRequestDto> servicos,

    List<@Valid PecaRequestDto> pecas
) {
}
