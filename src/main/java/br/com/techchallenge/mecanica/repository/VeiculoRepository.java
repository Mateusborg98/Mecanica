package br.com.techchallenge.mecanica.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {

    boolean existsByPlaca(String placa);

    Veiculo findByPlaca(String placa);

}
