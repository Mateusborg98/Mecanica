package br.com.techchallenge.mecanica.dto.clienteDto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResumoDto {

    private UUID id;
    private String nome;
    private String cpfCnpj;

}