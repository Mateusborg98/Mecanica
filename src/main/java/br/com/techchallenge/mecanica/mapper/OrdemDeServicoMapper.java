package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResumoDto;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;

@Component
public class OrdemDeServicoMapper {

    public OrdemDeServicoResponseDto toResponse(OrdemDeServico os) {

        if (os == null) {
            return null;
        }

        OrdemDeServicoResponseDto dto = new OrdemDeServicoResponseDto();
        dto.setId(os.getId());
        dto.setStatus(os.getStatus().name());
        dto.setCliente(os.getCliente().getNome());
        dto.setVeiculo(os.getVeiculo().getPlaca());
        dto.setValorTotal(os.getValorTotalOs());
        dto.setDtInicioOs(os.getDtInicioOs());

        return dto;
    }

    // =====================================================
    // ORDEM DE SERVIÇO → RESUMO
    // =====================================================
    public OrdemDeServicoResumoDto toResumo(OrdemDeServico os) {
        return new OrdemDeServicoResumoDto(
                os.getId(),
                os.getStatus().name(),
                os.getDtInicioOs(),
                os.getCliente().getNome(),
                os.getVeiculo().getPlaca());
    }
}