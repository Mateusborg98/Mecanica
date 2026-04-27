package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.servicoDto.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.servicoDto.UpdateServicoRequestDTO;
import br.com.techchallenge.mecanica.entity.Servico;

@Component
public class ServicoMapper {

    public Servico toEntity(CreateServicoRequestDto dto) {
        Servico servico = new Servico();
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        return servico;
    }

    public ServicoResponseDto toResponse(Servico servico) {
        return new ServicoResponseDto(
                servico.getId(),
                servico.getDescricao(),
                servico.getPreco());
    }

    public void updateEntity(UpdateServicoRequestDTO dto, Servico entity) {
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getValor());
    }

}