package br.com.techchallenge.mecanica.dto.clienteDto;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResumoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDetalhadoResponseDto {

    private UUID id;
    private String nome;
    private String cpfCnpj;
    private String contato;
    private String email;
    private List<VeiculoResumoDto> veiculos;
}
