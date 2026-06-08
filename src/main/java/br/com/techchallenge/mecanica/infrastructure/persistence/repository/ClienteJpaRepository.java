package br.com.techchallenge.mecanica.infrastructure.persistence.repository;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, UUID> {

    boolean existsByCpfCnpj(String cpfCnpj);

    Optional<ClienteJpaEntity> findByCpfCnpj(String cpfCnpj);

}
