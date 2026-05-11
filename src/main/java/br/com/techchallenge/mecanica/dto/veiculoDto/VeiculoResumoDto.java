package br.com.techchallenge.mecanica.dto.veiculoDto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoResumoDto {

    private UUID id;
    private String placa;
    private String modelo;

}