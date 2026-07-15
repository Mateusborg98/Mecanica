package br.com.techchallenge.mecanica.domain.servico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Servico {

    private UUID id;
    private String descricao;
    private BigDecimal preco;
    private boolean ativo;
    private LocalDateTime dataInativacao;

    public Servico(UUID id, String descricao, BigDecimal preco,
            boolean ativo, LocalDateTime dataInativacao) {
        validarDescricao(descricao);
        validarPreco(preco);
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.ativo = ativo;
        this.dataInativacao = dataInativacao;
    }

    public Servico(
            String descricao,
            BigDecimal preco) {

        validarDescricao(descricao);
        validarPreco(preco);

        this.descricao = descricao;
        this.preco = preco;
        this.ativo = true;
        this.dataInativacao = null;

    }

    public void atualizarDados(
            String descricao,
            BigDecimal preco) {

        validarDescricao(descricao);
        validarPreco(preco);

        this.descricao = descricao;
        this.preco = preco;
    }

    public void inativar() {
        if (!Boolean.TRUE.equals(this.ativo) && dataInativacao != null) {
            throw new RegraNegocioException(
                    "Serviço já está inativo");
        }
        this.ativo = false;
        this.dataInativacao = LocalDateTime.now();
    }

    private void validarDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {

            throw new RegraNegocioException(
                    "Descrição obrigatória");
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

        if (!(o instanceof Servico other)) {
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
