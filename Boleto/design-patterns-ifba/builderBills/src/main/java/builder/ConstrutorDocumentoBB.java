package builder;

public class ConstrutorDocumentoBB extends ConstrutorBaseDocumentoCobranca {

    public ConstrutorDocumentoBB() {
        super("001"); // Código do Banco do Brasil
    }

    @Override
    protected void validarDadosEspecificos() {
        if (documentoCobrancaObj.getCodigoAgencia() == null || !documentoCobrancaObj.getCodigoAgencia().matches("\\d{4}")) {
            throw new IllegalArgumentException("Código da Agência (BB) deve conter 4 dígitos numéricos.");
        }
        if (documentoCobrancaObj.getCodigoConta() == null || documentoCobrancaObj.getCodigoConta().isEmpty()) {
            throw new IllegalArgumentException("Código da Conta Corrente (BB) não foi informado.");
        }
        if (documentoCobrancaObj.getIdCarteiraCobranca() == null || documentoCobrancaObj.getIdCarteiraCobranca().isEmpty()) {
            throw new IllegalArgumentException("Identificador da Carteira de Cobrança (BB) não foi informado.");
        }

        String idCarteiraLimpo = documentoCobrancaObj.getIdCarteiraCobranca().replaceAll("[^0-9]", "");
        if (idCarteiraLimpo.length() != 2) {
            throw new IllegalArgumentException(
                    "Identificador da Carteira de Cobrança (BB) deve conter exatamente 2 dígitos numéricos: " + documentoCobrancaObj.getIdCarteiraCobranca());
        }
        documentoCobrancaObj.setIdCarteiraCobranca(idCarteiraLimpo);

        if (documentoCobrancaObj.getIdNossoNumero() == null || documentoCobrancaObj.getIdNossoNumero().isEmpty()) {
            throw new IllegalArgumentException("Identificador Nosso Número (BB) não foi informado.");
        }
    }

    @Override
    protected String gerarCampoLivre() {
        String idCarteiraFmt = formatarTextoNumerico(documentoCobrancaObj.getIdCarteiraCobranca(), 2);
        String idNossoNumFmt = formatarTextoNumerico(documentoCobrancaObj.getIdNossoNumero(), 11); // Para convênio de 7 posições, Nosso Número pode ter até 10 + DV. BB usa 11 aqui com DAC "0".
        String codAgenciaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoAgencia(), 4);
        String codContaFmt = formatarTextoNumerico(documentoCobrancaObj.getCodigoConta(), 7); // Exemplo, pode variar com convênio
        String digitoVerificadorCampoLivre = "0"; // DAC padrão para este formato do Banco do Brasil

        return idCarteiraFmt + idNossoNumFmt + codAgenciaFmt + codContaFmt + digitoVerificadorCampoLivre;
    }

    // Método auxiliar para formatação
    private String formatarTextoNumerico(String valorOriginal, int tamanhoFinal) {
        String somenteDigitos = valorOriginal.replaceAll("[^0-9]", "");
        return String.format("%" + tamanhoFinal + "s", somenteDigitos).replace(' ', '0');
    }
}