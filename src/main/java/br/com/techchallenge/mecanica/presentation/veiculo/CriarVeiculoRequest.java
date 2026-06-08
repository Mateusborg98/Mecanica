package br.com.techchallenge.mecanica.presentation.veiculo;

import br.com.techchallenge.mecanica.presentation.annotation.PlacaValida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

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
