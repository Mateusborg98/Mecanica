package br.com.techchallenge.mecanica.domain.cliente.valueobject;

import br.com.techchallenge.mecanica.domain.exception.CpfInvalidoException;

import java.util.Objects;

public class CpfCnpj {

    private final String valor;

    public CpfCnpj(String valor) throws CpfInvalidoException {
        String cpfNormalizado = normalizar(valor);

        validar(cpfNormalizado);

        this.valor = valor;
    }

    private String normalizar(String cpfCnpj) throws CpfInvalidoException {

        if (cpfCnpj == null) {
            throw new CpfInvalidoException("CPF não pode ser nulo");
        }

        return cpfCnpj.replaceAll("\\D", "");
    }

    private void validar(String cpfCnpj) throws CpfInvalidoException {

        if (cpfCnpj.length() != 11) {
            throw new CpfInvalidoException("CPF inválido");
        }

        if (cpfCnpj.matches("(\\d)\\1{10}")) {
            throw new CpfInvalidoException("CPF inválido");
        }

        if (!validarDigitos(cpfCnpj)) {
            throw new CpfInvalidoException("CPF inválido");
        }
    }

    private boolean validarDigitos(String cpfCnpj) {

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpfCnpj.charAt(i) - '0') * (10 - i);
        }

        int digito1 = 11 - (soma % 11);

        if (digito1 >= 10) digito1 = 0;

        if (digito1 != (cpfCnpj.charAt(9) - '0')) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += (cpfCnpj.charAt(i) - '0') * (11 - i);
        }

        int digito2 = 11 - (soma % 11);

        if (digito2 >= 10) digito2 = 0;

        return digito2 == (cpfCnpj.charAt(10) - '0');
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CpfCnpj cpfCnpj)) return false;

        return Objects.equals(valor, cpfCnpj.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}