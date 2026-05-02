package br.com.techchallenge.mecanica.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResumoDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Veiculo;

@Component
public class OrdemDeServicoMapper {
    
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
        OrdemDeServicoResponseDto dto = 
            new OrdemDeServicoResponseDto(os.getId(), os.getStatus(), os.getDtInicioOs(), 
                os.getDtFimOs(), os.getCliente(), os.getVeiculo(), null, os.getItens(), 
                os.getServicos());
        return dto;
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