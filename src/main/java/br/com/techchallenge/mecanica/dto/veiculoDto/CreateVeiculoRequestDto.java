package br.com.techchallenge.mecanica.dto.veiculoDto;

import java.util.UUID;

import br.com.techchallenge.mecanica.annotation.PlacaValida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVeiculoRequestDto {

    @NotBlank
    @PlacaValida
    private String placa;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotNull
    @Positive
    private Integer ano;

    @NotNull
    private UUID clienteId;

}