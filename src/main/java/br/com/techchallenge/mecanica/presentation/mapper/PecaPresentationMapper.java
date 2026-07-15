package br.com.techchallenge.mecanica.presentation.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.dto.peca.AtualizarPecaInput;
import br.com.techchallenge.mecanica.application.dto.peca.CriarPecaInput;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.presentation.dto.peca.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.PecaResponseDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.UpdatePecaRequestDto;

@Component
public class PecaPresentationMapper {

    public CriarPecaInput toInput(CreatePecaRequestDto request) {
        return new CriarPecaInput(
                request.getNome(),
                request.getMarca(),
                request.getPreco());
    }

    public AtualizarPecaInput toInput(UpdatePecaRequestDto request) {
        return new AtualizarPecaInput(
                request.getNome(),
                request.getMarca(),
                request.getPreco());
    }

    public PecaResponseDto toResponse(Peca peca) {
        return new PecaResponseDto(
                peca.getId(),
                peca.getNome(),
                peca.getMarca(),
                peca.getPreco());
    }
}
