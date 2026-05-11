package br.com.techchallenge.mecanica.dto.clienteDto;

import br.com.techchallenge.mecanica.annotation.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClienteRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    @CpfCnpj
    private String cpfCnpj;

    @NotBlank
    private String contato;

    @Email
    private String email;

}