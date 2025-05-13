package util;

public class CalculadoraDigitoVerificadorM11 {

    /**
     * Calcula o dígito verificador utilizando o algoritmo Módulo 11.
     * Pesos de 2 a 9, reiniciando, aplicados da direita para a esquerda.
     * Mapeamento de resultado específico: DV 0, 10 ou 11 resultam em 1.
     * @param sequenciaNumerica A sequência numérica base para o cálculo.
     * @return O dígito verificador calculado.
     */
    public static int computarDV(String sequenciaNumerica) {
        if (sequenciaNumerica == null || sequenciaNumerica.trim().isEmpty()) {
            throw new IllegalArgumentException("Sequência numérica não pode ser nula ou vazia para cálculo do Módulo 11.");
        }

        int acumuladorSoma = 0;
        int fatorPeso = 2; // Peso inicial (da direita para a esquerda)

        for (int i = sequenciaNumerica.length() - 1; i >= 0; i--) {
            char caractereAtual = sequenciaNumerica.charAt(i);
             if (!Character.isDigit(caractereAtual)) {
                throw new IllegalArgumentException("Sequência numérica contém caracteres não numéricos: " + caractereAtual + " em " + sequenciaNumerica );
            }
            int valorDigito = Character.getNumericValue(caractereAtual);
            acumuladorSoma += valorDigito * fatorPeso;

            fatorPeso++;
            if (fatorPeso > 9) {
                fatorPeso = 2; // Reinicia o ciclo de pesos
            }
        }

        int restoDivisao = acumuladorSoma % 11;
        int digitoVerificadorCalculado = 11 - restoDivisao;

        // Mapeamento específico para DV do código de barras FEBRABAN e alguns DACs
        if (digitoVerificadorCalculado == 0 || digitoVerificadorCalculado == 10 || digitoVerificadorCalculado == 11) {
            return 1;
        }

        return digitoVerificadorCalculado; // Retorna DV entre 2 e 9
    }
}