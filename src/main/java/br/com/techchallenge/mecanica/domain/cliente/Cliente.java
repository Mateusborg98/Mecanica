package br.com.techchallenge.mecanica.domain.cliente;

import br.com.techchallenge.mecanica.domain.cliente.valueobject.CpfCnpj;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private UUID id;
    private String nome;
    private CpfCnpj cpfCnpj;
    private String contato;
    private String email;

    public Cliente(String nome, CpfCnpj cpfCnpj, String contato, String email) {

        validarNome(nome);
        validarEmail(email);

        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.contato = contato;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void atualizarDados(String nome, String contato, String email) {

        validarNome(nome);
        validarEmail(email);

        this.nome = nome;
        this.contato = contato;
        this.email = email;
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }
    }

    private void validarEmail(String email) {

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
}
