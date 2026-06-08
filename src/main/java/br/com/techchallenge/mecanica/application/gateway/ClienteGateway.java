package br.com.techchallenge.mecanica.application.gateway;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteGateway {

    Cliente salvar(Cliente cliente) throws CpfInvalidoException;

    Optional<Cliente> buscarPorId(UUID id);

    Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj);

    List<Cliente> listar();

    void deletar(UUID id);
}