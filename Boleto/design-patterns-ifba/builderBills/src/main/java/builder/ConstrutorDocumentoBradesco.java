package builder;

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// IMPORT CORRIGIDO: Garanta que esta linha importe a classe correta
import util.CalculadoraDigitoVerificadorM11;
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

public class ConstrutorDocumentoBradesco extends ConstrutorBaseDocumentoCobranca {

    public ConstrutorDocumentoBradesco() {
        super("237"); // Código do Bradesco
    }

    @Override
    protected void validarDadosEspecificos() {
        if (documentoCobrancaObj.getCodigoAgencia() == null || !documentoCobrancaObj.getCodigoAgencia().matches("\\d{4}")) {
            throw new IllegalArgumentException("Código da Agência (Bradesco) deve conter 4 dígitos numéricos.");
        }
        if (documentoCobrancaObj.getCodigoConta() == null || documentoCobrancaObj.getCodigoConta().isEmpty()) {
            throw new IllegalArgumentException("Código da Conta Corrente (Bradesco) não foi informado.");
        }
        if (documentoCobrancaObj.getIdCarteiraCobranca() == null || documentoCobrancaObj.getIdCarteiraCobranca().isEmpty()) {
            throw new IllegalArgumentException("Identificador da Carteira de Cobrança (Bradesco) não foi informado.");
        }

        String idCarteiraLimpo = documentoCobrancaObj.getIdCarteiraCobranca().replaceAll("[^0-9]", "");
        if (idCarteiraLimpo.length() != 2) { // Bradesco geralmente usa 2 dígitos para carteira
            throw new IllegalArgumentException(
                    "Identificador da Carteira de Cobrança (Bradesco) deve conter exatamente 2 dígitos numéricos: " + documentoCobrancaObj.getIdCarteiraCobranca());
        }
        documentoCobrancaObj.setIdCarteiraCobranca(idCarteiraLimpo);

        if (documentoCobrancaObj.getIdNossoNumero() == null || documentoCobrancaObj.getIdNossoNumero().isEmpty()) {
            throw new IllegalArgumentException("Identificador Nosso Número (Bradesco) não foi informado.");
        }
        // Validação de tamanho do Nosso Número para Bradesco (geralmente 11 dígitos)
        String nossoNumeroLimpo = documentoCobrancaObj.getIdNossoNumero().replaceAll("[^0-9]", "");
        if (nossoNumeroLimpo.length() != 11) {
             throw new IllegalArgumentException("Nosso Número (Bradesco) deve conter 11 dígitos numéricos: " + documentoCobrancaObj.getIdNossoNumero());
        }
    }

    @Override
    protected String gerarCampoLivre() {
        String codAgenciaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoAgencia(), 4);
        String idCarteiraFmt = formatarTextoNumerico(documentoCobrancaObj.getIdCarteiraCobranca(), 2);
        String idNossoNumFmt = formatarTextoNumerico(documentoCobrancaObj.getIdNossoNumero(), 11);
        String codContaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoConta(), 7);

        // Bloco para cálculo do DAC do campo livre do Bradesco
        String blocoParaDac = codAgenciaFmt + idCarteiraFmt + idNossoNumFmt + codContaFmt;

        // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // USO CORRIGIDO: Garanta que a chamada use o nome correto da classe
        String digitoVerificadorCampoLivre = String.valueOf(CalculadoraDigitoVerificadorM11.computarDV(blocoParaDac));
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        String campoLivreGerado = codAgenciaFmt + idCarteiraFmt + idNossoNumFmt + codContaFmt + digitoVerificadorCampoLivre;

        if (campoLivreGerado.length() != 25) {
            throw new IllegalStateException("Campo Livre (Bradesco) deve ter 25 dígitos. Gerado: " + campoLivreGerado.length() + " -> " + campoLivreGerado);
        }
        return campoLivreGerado;
    }

    private String formatarTextoNumerico(String valorOriginal, int tamanhoFinal) {
        String somenteDigitos = valorOriginal.replaceAll("[^0-9]", "");
        return String.format("%" + tamanhoFinal + "s", somenteDigitos).replace(' ', '0');
    }
}