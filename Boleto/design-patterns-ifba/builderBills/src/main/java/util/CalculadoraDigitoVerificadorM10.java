package util;

public class CalculadoraDigitoVerificadorM10 {

    /**
     * Calcula o dígito verificador utilizando o algoritmo Módulo 10.
     * Pesos 2 e 1, alternando da direita para a esquerda.
     * @param sequenciaNumerica A sequência numérica base para o cálculo.
     * @return O dígito verificador calculado.
     */
    public static int computarDV(String sequenciaNumerica) {
        if (sequenciaNumerica == null || sequenciaNumerica.trim().isEmpty()) {
            throw new IllegalArgumentException("Sequência numérica não pode ser nula ou vazia para cálculo do Módulo 10.");
        }

        int acumuladorSoma = 0;
        int fatorPeso = 2; // Peso inicial (da direita para a esquerda)

        for (int i = sequenciaNumerica.length() - 1; i >= 0; i--) {
            char caractereAtual = sequenciaNumerica.charAt(i);
            if (!Character.isDigit(caractereAtual)) {
                throw new IllegalArgumentException("Sequência numérica contém caracteres não numéricos: " + caractereAtual);
            }
            int valorDigito = Character.getNumericValue(caractereAtual);
            int produtoParcial = valorDigito * fatorPeso;

            // Se o resultado da multiplicação for maior que 9, soma-se os algarismos
            if (produtoParcial > 9) {
                produtoParcial = (produtoParcial / 10) + (produtoParcial % 10);
            }

            acumuladorSoma += produtoParcial;

            // Alterna o peso
            fatorPeso = (fatorPeso == 2) ? 1 : 2;
        }

        int restoDivisao = acumuladorSoma % 10;
        int digitoVerificadorFinal = (restoDivisao == 0) ? 0 : (10 - restoDivisao);

        return digitoVerificadorFinal;
    }
}