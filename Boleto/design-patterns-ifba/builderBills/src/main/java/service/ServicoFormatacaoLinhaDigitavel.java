package service;

// Import corrigido para a classe refatorada
import util.CalculadoraDigitoVerificadorM10;

public class ServicoFormatacaoLinhaDigitavel {

    /**
     * Gera a linha digitável formatada a partir de um código de barras de 44 posições.
     * A lógica de formação dos campos e cálculo dos DVs segue o padrão FEBRABAN.
     *
     * @param numeroCodigoBarras String contendo os 44 dígitos do código de barras.
     * @return A linha digitável formatada (47 ou 48 caracteres, dependendo da representação).
     * @throws IllegalArgumentException Se o código de barras for inválido.
     */
    public static String montarLinhaDigitavelFormatada(String numeroCodigoBarras) {
        if (numeroCodigoBarras == null || numeroCodigoBarras.length() != 44) {
            throw new IllegalArgumentException("Número do Código de Barras fornecido é inválido (deve ter 44 dígitos).");
        }

        // Campo 1 da Linha Digitável (posições 1-3 do CB + pos 4 do CB + pos 20-24 do CB)
        // AAABC.CCCCX (A=Banco, B=Moeda, C=CampoLivre[1-5])
        String blocoBase1 = numeroCodigoBarras.substring(0, 3) +  // Código do Banco (Pos 1-3 do CB)
                            numeroCodigoBarras.substring(3, 4) +  // Código da Moeda (Pos 4 do CB)
                            numeroCodigoBarras.substring(19, 24); // 5 primeiros dígitos do Campo Livre (Pos 20-24 do CB)

        // Campo 2 da Linha Digitável (posições 25-34 do CB)
        // DDDDD.DDDDDY (D=CampoLivre[6-15])
        String blocoBase2 = numeroCodigoBarras.substring(24, 34); // 6º ao 15º dígito do Campo Livre (Pos 25-34 do CB)

        // Campo 3 da Linha Digitável (posições 35-44 do CB)
        // EEEEE.EEEEEZ (E=CampoLivre[16-25])
        String blocoBase3 = numeroCodigoBarras.substring(34, 44); // 16º ao 25º dígito do Campo Livre (Pos 35-44 do CB)

        // Campo 4 da Linha Digitável (Dígito Verificador Geral do Código de Barras)
        // K (K=DV Geral, Pos 5 do CB)
        String digitoVerificadorGeralCB = numeroCodigoBarras.substring(4, 5); // DV Geral (Pos 5 do CB)

        // Campo 5 da Linha Digitável (Fator de Vencimento + Valor)
        // UUUUVVVVVVVVVV (U=Fator Vencimento, V=Valor)
        String fatorVencimentoEValor = numeroCodigoBarras.substring(5, 9) +   // Fator de Vencimento (Pos 6-9 do CB)
                                       numeroCodigoBarras.substring(9, 19);  // Valor (Pos 10-19 do CB)

        // Calcula os DVs dos três primeiros blocos usando Módulo 10
        int dvBloco1 = CalculadoraDigitoVerificadorM10.computarDV(blocoBase1);
        int dvBloco2 = CalculadoraDigitoVerificadorM10.computarDV(blocoBase2);
        int dvBloco3 = CalculadoraDigitoVerificadorM10.computarDV(blocoBase3);

        // Formata a linha digitável completa
        // Formato: AAABC.CCCCX DDDDD.DDDDDY EEEEE.EEEEEZ K UUUUVVVVVVVVVV
        // Bloco1 (9 digitos) + DV1: AAABC CCCC + DV1 (5 + 4 + 1)
        // Bloco2 (10 digitos) + DV2: DDDDD DDDDD + DV2 (5 + 5 + 1)
        // Bloco3 (10 digitos) + DV3: EEEEE EEEEE + DV3 (5 + 5 + 1)
        return String.format("%s%s%d %s%s%d %s%s%d %s %s",
                blocoBase1.substring(0, 5), // AAABC
                blocoBase1.substring(5),    // CCCC (4 dígitos)
                dvBloco1,                   // X
                blocoBase2.substring(0, 5), // DDDDD
                blocoBase2.substring(5),    // DDDDD (5 dígitos)
                dvBloco2,                   // Y
                blocoBase3.substring(0, 5), // EEEEE
                blocoBase3.substring(5),    // EEEEE (5 dígitos)
                dvBloco3,                   // Z
                digitoVerificadorGeralCB,   // K
                fatorVencimentoEValor);     // UUUUVVVVVVVVVV
    }
}