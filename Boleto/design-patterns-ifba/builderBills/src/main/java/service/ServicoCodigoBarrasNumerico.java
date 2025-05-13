package service;

// Imports atualizados para os nomes refatorados
import java.math.BigDecimal;
import java.util.Date;

import util.CalculadoraDigitoVerificadorM11;
import util.UtilitarioDatasCobranca;

public class ServicoCodigoBarrasNumerico {

    public static String montarCodigoBarras(String codBancoInstituicao, Date dataVenc,
                                           BigDecimal valorDocumento, String textoCampoLivre) {

        checarConsistenciaDadosEntrada(codBancoInstituicao, dataVenc, valorDocumento, textoCampoLivre);

        String fatorVencFormatado = UtilitarioDatasCobranca.obterFatorVencimento(dataVenc);
        String valorDocFormatado = formatarQuantiaParaCodigo(valorDocumento);

        // Montagem inicial do código de barras com placeholder para o DV geral
        StringBuilder construtorCodigo = new StringBuilder()
                .append(codBancoInstituicao) // Posições 1-3
                .append("9")                 // Posição 4: Código da Moeda (Real)
                .append("0")                 // Posição 5: Placeholder para o DV Geral
                .append(fatorVencFormatado)  // Posições 6-9
                .append(valorDocFormatado)   // Posições 10-19
                .append(textoCampoLivre);    // Posições 20-44

        // Cálculo do DV Geral (Módulo 11) sobre as 43 posições (o placeholder '0' não afeta a soma)
        int digitoVerificadorGeral = CalculadoraDigitoVerificadorM11.computarDV(construtorCodigo.toString());

        // Insere o DV Geral na 5ª posição (índice 4)
        construtorCodigo.setCharAt(4, Character.forDigit(digitoVerificadorGeral, 10));

        return construtorCodigo.toString();
    }

    private static void checarConsistenciaDadosEntrada(String codBanco, Date dtVenc,
                                                      BigDecimal valDoc, String txtCampoLivre) {
        if (codBanco == null || codBanco.length() != 3) {
            throw new IllegalArgumentException("Código do banco inválido (deve ter 3 dígitos).");
        }
        if (dtVenc == null) {
            // Conforme DataUtil, fator será 0000, mas aqui validamos se é aceitável para o serviço.
            // Dependendo da regra de negócio, uma data de vencimento nula pode ser inaceitável.
            // Por ora, mantemos a lógica que permite, mas um log ou exceção mais específica poderia ser útil.
        }
        if (valDoc == null || valDoc.compareTo(BigDecimal.ZERO) < 0) { // Permite valor ZERO se for o caso
            throw new IllegalArgumentException("Valor do documento é inválido (não pode ser nulo ou negativo).");
        }
        if (txtCampoLivre == null || txtCampoLivre.length() != 25) {
            throw new IllegalArgumentException("Campo Livre deve ter exatamente 25 dígitos.");
        }
    }

    private static String formatarQuantiaParaCodigo(BigDecimal quantiaDocumento) {
        if (quantiaDocumento == null || quantiaDocumento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do documento para formatação é inválido.");
        }
        // Converte para centavos e formata com 10 dígitos, preenchendo com zeros à esquerda
        long valorEmCentavos = quantiaDocumento.multiply(new BigDecimal("100")).longValue();
        return String.format("%010d", valorEmCentavos);
    }
}