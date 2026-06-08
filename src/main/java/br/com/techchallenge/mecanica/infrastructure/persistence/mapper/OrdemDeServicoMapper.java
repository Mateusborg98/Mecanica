package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.operador.Operador;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrdemDeServicoMapper {

    private final PecaOrdemDeServicoMapper pecaOrdemDeServicoMapper;
    private final ServicoOrdemDeServicoMapper servicoOrdemDeServicoMapper;

    public OrdemDeServicoJpaEntity toEntity(OrdemDeServico os) {

        ClienteJpaEntity clienteJpaEntity = new ClienteJpaEntity();
        clienteJpaEntity.setId(os.getClienteId());

        VeiculoJpaEntity veiculoJpaEntity = new VeiculoJpaEntity();
        veiculoJpaEntity.setId(os.getVeiculoId());

        OperadorJpaEntity operadorJpaEntity = new OperadorJpaEntity();
        operadorJpaEntity.setId(os.getOperadorId());

        List<PecaOrdemDeServicoJpaEntity> pecaOrdemDeServicoJpaEntities
            = pecaOrdemDeServicoMapper.toListJpaEntity(os.getPecas());

        List<ServicoOrdemDeServicoJpaEntity> servicoOrdemDeServicoJpaEntities
            = servicoOrdemDeServicoMapper.toListJpaEntity(os.getServicos());

        return OrdemDeServicoJpaEntity.builder()
                .id(os.getId())
                .clienteJpaEntity(clienteJpaEntity)
                .veiculoJpaEntity(veiculoJpaEntity)
                .operadorJpaEntity(operadorJpaEntity)
                .status(os.getStatus())
                .dtInicioOs(os.getDtInicioOs())
                .dtFimOs(os.getDtFimOs())
                .valorTotalOs(os.getValorTotalOs())
                .pecas(pecaOrdemDeServicoJpaEntities)
                .servicos(servicoOrdemDeServicoJpaEntities)
                .build();
    }

    public OrdemDeServico toDomain(OrdemDeServicoJpaEntity osJpaEntity) {

        Cliente cliente = new Cliente();
        cliente.setId(osJpaEntity.getClienteJpaEntity().getId());

        Veiculo veiculo = new Veiculo();
        veiculo.setId(osJpaEntity.getVeiculoJpaEntity().getId());

        Operador operador = new Operador();
        operador.setId(osJpaEntity.getOperadorJpaEntity().getId());

        List<PecaOrdemDeServico> pecaOrdemDeServicos
            = pecaOrdemDeServicoMapper.toListDomain(osJpaEntity.getPecas());

        List<ServicoOrdemDeServico> servicoOrdemDeServicos
            = servicoOrdemDeServicoMapper.toListDomain(osJpaEntity.getServicos());

        return OrdemDeServico.builder()
                .id(osJpaEntity.getId())
                .clienteId(cliente.getId())
                .veiculoId(veiculo.getId())
                .operadorId(operador.getId())
                .status(osJpaEntity.getStatus())
                .dtInicioOs(osJpaEntity.getDtInicioOs())
                .dtFimOs(osJpaEntity.getDtFimOs())
                .valorTotalOs(osJpaEntity.getValorTotalOs())
                .pecas(pecaOrdemDeServicos)
                .servicos(servicoOrdemDeServicos)
                .build();
    }
}