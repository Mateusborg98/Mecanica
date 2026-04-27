package br.com.techchallenge.mecanica.mapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResumoDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Veiculo;

@Component
public class OrdemDeServicoMapper {

    private final ItemOrdemDeServicoMapper itemMapper;
    private final ServicoMapper servicoMapper;

    public OrdemDeServicoMapper(
            ItemOrdemDeServicoMapper itemMapper,
            ServicoMapper servicoMapper) {
        this.itemMapper = itemMapper;
        this.servicoMapper = servicoMapper;
    }

    public OrdemDeServico toEntity(
            CreateOrdemDeServicoRequestDto request,
            Veiculo veiculo,
            Cliente cliente) {
        OrdemDeServico ordem = new OrdemDeServico();

        ordem.setVeiculo(veiculo);
        ordem.setCliente(cliente);
        ordem.setItens(request.getItens());
        ordem.setServicos(request.getServicos());
        ordem.setDtInicioOs(LocalDateTime.now());

        return ordem;
    }

    public OrdemDeServicoResponseDto toResponse(OrdemDeServico os) {
        return new OrdemDeServicoResponseDto(
                os.getId(),
                os.getStatus().name(),
                os.getDtInicioOs(),
                os.getDtFimOs(),
                ClienteMapper.toResumo(os.getCliente()),
                VeiculoMapper.toResumo(os.getVeiculo()),
                OperadorMapper.toResumo(os.getOperador()),
                os.getItens().stream()
                        .map(itemMapper::toResponse)
                        .collect(Collectors.toList()),
                os.getServicos().stream()
                        .map(servicoMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    public OrdemDeServicoResumoDto toResumo(OrdemDeServico os) {
        return new OrdemDeServicoResumoDto(
                os.getId(),
                os.getStatus().name(),
                os.getDtInicioOs(),
                os.getCliente().getNome(),
                os.getVeiculo().getPlaca());
    }
}