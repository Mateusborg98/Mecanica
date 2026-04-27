package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResumoDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResumoDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Veiculo;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(CreateVeiculoRequestDto dto, Cliente cliente) {
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(
                dto.getPlaca()
                        .toUpperCase()
                        .replaceAll("[^A-Z0-9]", ""));
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setCliente(cliente);
        return veiculo;
    }

    public VeiculoResponseDto toResponse(Veiculo veiculo) {
        return new VeiculoResponseDto(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                new ClienteResumoDto(
                        veiculo.getCliente().getId(),
                        veiculo.getCliente().getNome(),
                        veiculo.getCliente().getCpfCnpj()));
    }

    public static VeiculoResumoDto toResumo(Veiculo veiculo) {
        return new VeiculoResumoDto(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo());
    }

    public void updateEntity(UpdateVeiculoRequestDto dto, Veiculo veiculo) {
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
    }

}
