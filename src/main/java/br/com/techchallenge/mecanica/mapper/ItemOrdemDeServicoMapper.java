package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto.CreateItemOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto.ItemOrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.entity.PecaOrdemDeServico;

@Component
public class ItemOrdemDeServicoMapper {

    public PecaOrdemDeServico toEntity(
            CreateItemOrdemDeServicoRequestDto dto,
            Peca peca,
            OrdemDeServico os) {
        PecaOrdemDeServico item = new PecaOrdemDeServico();
        item.setPeca(peca);
        item.setQuantidade(dto.getQuantidade());
        item.setOrdemDeServico(os);
        return item;
    }

    public ItemOrdemDeServicoResponseDto toResponse(PecaOrdemDeServico item) {
        return new ItemOrdemDeServicoResponseDto(
                item.getId(),
                item.getPeca().getNome(),
                item.getQuantidade(),
                item.getPeca().getPreco(),
                item.getPeca().getPreco()
                        .multiply(
                                java.math.BigDecimal.valueOf(item.getQuantidade())));
    }
}