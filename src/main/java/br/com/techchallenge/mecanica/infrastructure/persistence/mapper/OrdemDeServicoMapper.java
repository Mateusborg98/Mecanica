package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrdemDeServicoMapper {

    private final PecaOrdemDeServicoMapper pecaOrdemDeServicoMapper;
    private final ServicoOrdemDeServicoMapper servicoOrdemDeServicoMapper;

    public OrdemDeServicoJpaEntity toEntity(OrdemDeServico os) {

        OrdemDeServicoJpaEntity entity = OrdemDeServicoJpaEntity.builder()
                .id(os.getId())
                .clienteJpaEntity(
                        ClienteJpaEntity.builder()
                                .id(os.getClienteId())
                                .build())
                .veiculoJpaEntity(
                        VeiculoJpaEntity.builder()
                                .id(os.getVeiculoId())
                                .build())
                .operadorJpaEntity(
                        OperadorJpaEntity.builder()
                                .id(os.getOperadorId())
                                .build())
                .status(os.getStatus())
                .dtInicioOs(os.getDtInicioOs())
                .dtFimOs(os.getDtFimOs())
                .valorTotalOs(os.getValorTotalOs())
                .build();

        var pecas = pecaOrdemDeServicoMapper.toListJpaEntity(os.getPecas());
        pecas.forEach(item -> item.setOrdemDeServicoJpaEntity(entity));
        entity.setPecas(pecas);

        var servicos = servicoOrdemDeServicoMapper.toListJpaEntity(os.getServicos());
        servicos.forEach(item -> item.setOrdemDeServicoJpaEntity(entity));
        entity.setServicos(servicos);

        return entity;
    }

    public OrdemDeServico toDomain(OrdemDeServicoJpaEntity osJpaEntity) {

        List<PecaOrdemDeServico> pecas = pecaOrdemDeServicoMapper.toListDomain(osJpaEntity.getPecas());

        List<ServicoOrdemDeServico> servicos = servicoOrdemDeServicoMapper.toListDomain(osJpaEntity.getServicos());

        return new OrdemDeServico(
                osJpaEntity.getId(),
                osJpaEntity.getClienteJpaEntity().getId(),
                osJpaEntity.getVeiculoJpaEntity().getId(),
                osJpaEntity.getOperadorJpaEntity().getId(),
                osJpaEntity.getStatus(),
                osJpaEntity.getDtInicioOs(),
                osJpaEntity.getDtFimOs(),
                osJpaEntity.getValorTotalOs(),
                pecas,
                servicos);
    }
}
