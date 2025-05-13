package builder;

// Import corrigido para o nome da classe refatorada
import util.CalculadoraDigitoVerificadorM10;

public class ConstrutorDocumentoItau extends ConstrutorBaseDocumentoCobranca {

    public ConstrutorDocumentoItau() {
        super("341"); // Código do Itaú
    }

    @Override
    protected void validarDadosEspecificos() {
        if (documentoCobrancaObj.getCodigoAgencia() == null || !documentoCobrancaObj.getCodigoAgencia().matches("\\d{4}")) {
            throw new IllegalArgumentException("Código da Agência (Itaú) deve conter 4 dígitos numéricos.");
        }
        if (documentoCobrancaObj.getCodigoConta() == null || documentoCobrancaObj.getCodigoConta().isEmpty()) {
            throw new IllegalArgumentException("Código da Conta Corrente (Itaú) não foi informado.");
        }
        // Itaú geralmente usa 5 dígitos para conta + 1 DV da conta, aqui pedimos a conta sem DV.
        // A validação do tamanho da conta pode ser mais específica dependendo do layout.

        if (documentoCobrancaObj.getIdCarteiraCobranca() == null || documentoCobrancaObj.getIdCarteiraCobranca().isEmpty()) {
            throw new IllegalArgumentException("Identificador da Carteira de Cobrança (Itaú) não foi informado.");
        }
        String idCarteiraLimpo = documentoCobrancaObj.getIdCarteiraCobranca().replaceAll("[^0-9]", "");
        if (idCarteiraLimpo.length() != 3) { // Itaú usa 3 dígitos para carteira
            throw new IllegalArgumentException(
                    "Identificador da Carteira de Cobrança (Itaú) deve conter exatamente 3 dígitos numéricos: " + documentoCobrancaObj.getIdCarteiraCobranca());
        }
        documentoCobrancaObj.setIdCarteiraCobranca(idCarteiraLimpo);

        if (documentoCobrancaObj.getIdNossoNumero() == null || documentoCobrancaObj.getIdNossoNumero().isEmpty()) {
            throw new IllegalArgumentException("Identificador Nosso Número (Itaú) não foi informado.");
        }
        // Itaú usa 8 dígitos para Nosso Número nesta composição do campo livre
        String nossoNumeroLimpo = documentoCobrancaObj.getIdNossoNumero().replaceAll("[^0-9]", "");
        if (nossoNumeroLimpo.length() != 8) {
             throw new IllegalArgumentException("Nosso Número (Itaú) para este layout deve conter 8 dígitos numéricos: " + documentoCobrancaObj.getIdNossoNumero());
        }
    }

    @Override
    protected String gerarCampoLivre() {
        String idCarteiraFmt = formatarTextoNumerico(documentoCobrancaObj.getIdCarteiraCobranca(), 3);
        String idNossoNumFmt = formatarTextoNumerico(documentoCobrancaObj.getIdNossoNumero(), 8);
        String codAgenciaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoAgencia(), 4);
        String codContaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoConta(), 5); // Conta com 5 posições no campo livre

        // Cálculo do DAC (Módulo 10) para (Carteira + Nosso Número) - Uso da classe corrigida
        String dacCarteiraNossoNumero = String.valueOf(CalculadoraDigitoVerificadorM10.computarDV(idCarteiraFmt + idNossoNumFmt));

        // Cálculo do DAC (Módulo 10) para (Agência + Conta Corrente) - Uso da classe corrigida
        String dacAgenciaConta = String.valueOf(CalculadoraDigitoVerificadorM10.computarDV(codAgenciaFmt + codContaFmt));

        String sufixoCampoLivre = "000"; // Zeros complementares para o campo livre do Itaú

        String campoLivreGerado = idCarteiraFmt + idNossoNumFmt + dacCarteiraNossoNumero +
                                  codAgenciaFmt + codContaFmt + dacAgenciaConta + sufixoCampoLivre;

        if (campoLivreGerado.length() != 25) {
             throw new IllegalStateException("Campo Livre (Itaú) deve ter 25 dígitos. Gerado: " + campoLivreGerado.length() + " -> " + campoLivreGerado);
        }
        return campoLivreGerado;
    }

    private String formatarTextoNumerico(String valorOriginal, int tamanhoFinal) {
        String somenteDigitos = valorOriginal.replaceAll("[^0-9]", "");
        return String.format("%" + tamanhoFinal + "s", somenteDigitos).replace(' ', '0');
    }
}