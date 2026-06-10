package br.com.techchallenge.mecanica.domain.veiculo;

import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.domain.veiculo.valueObject.Placa;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {
    private UUID id;
    private Placa placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private Cliente cliente;

    public Veiculo(Placa placa, String marca, String modelo, Integer ano, Cliente cliente) {

        validarMarca(marca);
        validarModelo(modelo);
        validarAno(ano);

        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cliente = cliente;
    }

    public void atualizarDados(Placa placa, String marca, String modelo, Integer ano) {

        validarMarca(marca);
        validarModelo(modelo);
        validarAno(ano);

        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public void atualizarIdCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    private void validarMarca(String marca) {

        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("Marcar obrigatório");
        }
    }

    private void validarModelo(String modelo) {

        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("Modelo obrigatório");
        }
    }

    private void validarAno(Integer ano) {

        if (ano <= 0 || ano <= 1900 || ano > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Ano inválido");
        }
    }
}
