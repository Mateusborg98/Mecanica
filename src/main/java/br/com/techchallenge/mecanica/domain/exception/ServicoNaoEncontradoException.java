package br.com.techchallenge.mecanica.domain.exception;

public class ServicoNaoEncontradoException extends RuntimeException {

    public ServicoNaoEncontradoException(String message) {
        super(message);
    }

}
