package br.com.techchallenge.mecanica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByCpfCnpj(String cpfCnpj);

    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    
}
