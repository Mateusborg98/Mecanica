package br.com.techchallenge.mecanica.dto.clienteDto;

import java.util.UUID;

public class ClienteResumoDto {

    private UUID id;
    private String nome;
    private String cpfCnpj;

    public ClienteResumoDto(UUID id, String nome, String cpfCnpj) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
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

}