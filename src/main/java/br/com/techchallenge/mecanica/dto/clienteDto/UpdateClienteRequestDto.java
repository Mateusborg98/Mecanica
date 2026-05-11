package br.com.techchallenge.mecanica.dto.clienteDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClienteRequestDto {

    private String nome;
    private String contato;
    private String email;

}