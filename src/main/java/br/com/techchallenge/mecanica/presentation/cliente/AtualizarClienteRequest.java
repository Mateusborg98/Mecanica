package br.com.techchallenge.mecanica.presentation.cliente;

public record AtualizarClienteRequest(
        String nome,
        String contato,
        String email
) {
}
