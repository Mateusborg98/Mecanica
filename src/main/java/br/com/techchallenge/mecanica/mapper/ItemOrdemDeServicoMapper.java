package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto.CreateItemOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto.ItemOrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.entity.ItemOrdemDeServico;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;

@Component
public class ItemOrdemDeServicoMapper {

    public ItemOrdemDeServico toEntity(
            CreateItemOrdemDeServicoRequestDto dto,
            Peca peca,
            OrdemDeServico os) {
        ItemOrdemDeServico item = new ItemOrdemDeServico();
        item.setPeca(peca);
        item.setQuantidade(dto.getQuantidade());
        item.setValorUnitario(dto.getValorUnitario());
        item.setOrdemDeServico(os);
        return item;
    }

    public ItemOrdemDeServicoResponseDto toResponse(ItemOrdemDeServico item) {
        return new ItemOrdemDeServicoResponseDto(
                item.getId(),
                item.getPeca().getNome(),
                item.getQuantidade(),
                item.getValorUnitario(),
                item.getValorUnitario()
                        .multiply(
                                java.math.BigDecimal.valueOf(item.getQuantidade())));
    }
}