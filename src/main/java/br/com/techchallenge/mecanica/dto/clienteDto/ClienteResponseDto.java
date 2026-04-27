package br.com.techchallenge.mecanica.dto.clienteDto;

import java.util.UUID;

public class ClienteResponseDto {

    private UUID id;
    private String nome;
    private String cpfCnpj;
    private String contato;
    private String email;

    public ClienteResponseDto(UUID id, String nome, String cpfCnpj, String contato, String email) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.contato = contato;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getContato() {
        return contato;
    }

    public String getEmail() {
        return email;
    }

}
