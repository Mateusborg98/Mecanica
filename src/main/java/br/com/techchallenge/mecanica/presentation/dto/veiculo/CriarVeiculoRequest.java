package br.com.techchallenge.mecanica.presentation.dto.veiculo;

import java.util.UUID;

import br.com.techchallenge.mecanica.presentation.annotation.PlacaValida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarVeiculoRequest(
        @NotBlank
        @PlacaValida
        String placa,

        @NotBlank
        String marca,

        @NotBlank
        String modelo,

        @NotNull
        @Positive
        Integer ano,

        @NotNull
        UUID clienteId
) {
}
