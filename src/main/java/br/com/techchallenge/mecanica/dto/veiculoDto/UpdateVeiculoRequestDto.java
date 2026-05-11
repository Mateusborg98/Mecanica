package br.com.techchallenge.mecanica.dto.veiculoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVeiculoRequestDto {

    private String marca;
    private String modelo;
    private Integer ano;

}