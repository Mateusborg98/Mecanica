package br.com.techchallenge.mecanica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Operador;

public interface OperadorRepository extends JpaRepository<Operador, UUID> {

    Optional<Operador> findByMatricula(Integer matricula);
    
}
