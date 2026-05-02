package br.com.techchallenge.mecanica.dto.clienteDto;

import br.com.techchallenge.mecanica.annotation.CpfCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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

    public CreateClienteRequestDto(@NotBlank String nome, @NotBlank String cpfCnpj, @NotBlank String contato,
            @Email String email) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.contato = contato;
        this.email = email;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}