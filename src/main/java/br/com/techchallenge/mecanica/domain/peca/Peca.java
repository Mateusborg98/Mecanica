package br.com.techchallenge.mecanica.domain.peca;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Peca {

    private UUID id;
    private String nome;
    private String marca;
    private BigDecimal preco;
    private boolean ativo;
    private LocalDateTime dataInativacao;

    public Peca(String nome, String marca, BigDecimal preco) {
        validarNome(nome);
        validarPreco(preco);

        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.ativo = true;
        this.dataInativacao = null;
    }

    public Peca(
            UUID id,
            String nome,
            String marca,
            BigDecimal preco) {

        validarNome(nome);
        validarPreco(preco);

        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.ativo = true;
        this.dataInativacao = null;
    }

    public void atualizarDados(
            String nome,
            String marca,
            BigDecimal preco) {

        validarNome(nome);
        validarPreco(preco);

        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
    }

    public void inativar() {
        if (!ativo) {
            throw new PecaNaoEncontradaException(
                    "Peca já está inativo");
        }
        this.ativo = false;
        this.dataInativacao = LocalDateTime.now();
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new RegraNegocioException(
                    "Nome da peça obrigatório");
        }
    }

    private void validarPreco(BigDecimal preco) {

        if (preco == null
                || preco.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RegraNegocioException(
                    "Preço inválido");
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Peca other)) {
            return false;
        }

        return id != null
                && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}