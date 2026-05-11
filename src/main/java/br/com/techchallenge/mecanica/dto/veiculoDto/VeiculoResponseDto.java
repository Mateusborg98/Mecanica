package br.com.techchallenge.mecanica.dto.veiculoDto;

import java.util.UUID;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResumoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoResponseDto {

    private UUID id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private ClienteResumoDto cliente;

}