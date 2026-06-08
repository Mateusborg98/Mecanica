package br.com.techchallenge.mecanica.application.gateway;

import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;
import br.com.techchallenge.mecanica.domain.exception.PlacaInvalidaException;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoGateway {

    Veiculo salvar(Veiculo request) throws PlacaInvalidaException, CpfInvalidoException;

    Optional<Veiculo> buscarPorId(UUID id);

    Optional<Veiculo> buscarPorPlaca(String placa);

    List<Veiculo> listar();

    void deletar(UUID id);
}