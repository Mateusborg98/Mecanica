package br.com.techchallenge.mecanica.dto.clienteDto;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResumoDto;

public class ClienteDetalhadoResponseDto {

    private UUID id;
    private String nome;
    private String cpfCnpj;
    private String contato;
    private String email;
    private List<VeiculoResumoDto> veiculos;

    public ClienteDetalhadoResponseDto(
            UUID id,
            String nome,
            String cpfCnpj,
            String contato,
            String email,
            List<VeiculoResumoDto> veiculos) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.contato = contato;
        this.email = email;
        this.veiculos = veiculos;
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

    public List<VeiculoResumoDto> getVeiculos() {
        return veiculos;
    }

}
