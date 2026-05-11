package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.dto.pecaDto.UpdatePecaRequestDto;
import br.com.techchallenge.mecanica.entity.Peca;

@Component
public class PecaMapper {

    public Peca toEntity(CreatePecaRequestDto dto) {
        Peca peca = new Peca();
        peca.setMarca(dto.getMarca());
        peca.setNome(dto.getNome());
        peca.setPreco(dto.getPreco());
        return peca;
    }

    public void updateEntity(UpdatePecaRequestDto dto, Peca entity) {
        entity.setNome(dto.getNome());
        entity.setMarca(dto.getMarca());
        entity.setPreco(dto.getPreco());
    }

    public PecaResponseDto toResponseDto(Peca entity) {
        return new PecaResponseDto(
        entity.getId(),
        entity.getNome(),
        entity.getMarca(),
        entity.getPreco());
    }
}
