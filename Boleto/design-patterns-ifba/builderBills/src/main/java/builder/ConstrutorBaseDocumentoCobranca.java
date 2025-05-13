package builder;

// Imports atualizados
import java.math.BigDecimal;
import java.util.Date;

import model.DocumentoCobranca;
import model.PagadorInfo;
import model.RecebedorInfo; // <<< Import ADICIONADO
import service.ServicoCodigoBarrasNumerico;
import service.ServicoFormatacaoLinhaDigitavel;

public abstract class ConstrutorBaseDocumentoCobranca implements ConstrutorDocumentoCobranca {

    protected final DocumentoCobranca documentoCobrancaObj;

    public ConstrutorBaseDocumentoCobranca(String codigoBancoInst) {
        this.documentoCobrancaObj = new DocumentoCobranca();
        this.documentoCobrancaObj.setCodigoBancoInstituicao(codigoBancoInst);
    }

    @Override
    public ConstrutorDocumentoCobranca comRecebedor(String nomeRec, String docRec, String endRec) {
        this.documentoCobrancaObj.setDadosRecebedor(new RecebedorInfo(nomeRec, docRec, endRec));
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comPagador(String nomePag, String docPag, String endPag) {
        this.documentoCobrancaObj.setDadosPagador(new PagadorInfo(nomePag, docPag, endPag));
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comDadosDocumentoOriginador(String idDocOrig, Date dtEmissao) {
        this.documentoCobrancaObj.setIdDocumentoOriginador(idDocOrig);
        this.documentoCobrancaObj.setDataEmissao(dtEmissao);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comDataLimitePagamento(Date dtLimite) {
        this.documentoCobrancaObj.setDataLimitePagamento(dtLimite);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comValorNominal(BigDecimal quantiaNominal) {
        this.documentoCobrancaObj.setValorNominal(quantiaNominal);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comTextoInstrucoes(String instrucoes) {
        this.documentoCobrancaObj.setTextoInstrucoes(instrucoes);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comDescricaoLocalPagamento(String local) {
        this.documentoCobrancaObj.setDescricaoLocalPagamento(local);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comValorAbatimento(BigDecimal valAbatimento) {
        this.documentoCobrancaObj.setValorAbatimento(valAbatimento);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comValorPenalidade(BigDecimal valPenalidade) {
        this.documentoCobrancaObj.setValorPenalidade(valPenalidade);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comValorAcrescimos(BigDecimal valAcrescimos) {
        this.documentoCobrancaObj.setValorAcrescimos(valAcrescimos);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comCodigoAgencia(String codAgencia) {
        this.documentoCobrancaObj.setCodigoAgencia(codAgencia);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comCodigoConta(String codConta) {
        this.documentoCobrancaObj.setCodigoConta(codConta);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comIdCarteiraCobranca(String idCarteira) {
        this.documentoCobrancaObj.setIdCarteiraCobranca(idCarteira);
        return this;
    }

    @Override
    public ConstrutorDocumentoCobranca comIdNossoNumero(String idNossoNum) {
        this.documentoCobrancaObj.setIdNossoNumero(idNossoNum);
        return this;
    }

    @Override
    public DocumentoCobranca construir() {
        validarDadosComuns();
        validarDadosEspecificos(); // Método abstrato, nome mantido

        // Gera o código de barras numérico
        String numCodBarras = ServicoCodigoBarrasNumerico.montarCodigoBarras(
                documentoCobrancaObj.getCodigoBancoInstituicao(),
                documentoCobrancaObj.getDataLimitePagamento(),
                documentoCobrancaObj.getValorNominal(),
                gerarCampoLivre() // Método abstrato, nome mantido
        );

        // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        // Gera a linha digitável formatada a partir do código de barras
        String textoLinhaDigitavel = ServicoFormatacaoLinhaDigitavel.montarLinhaDigitavelFormatada(numCodBarras);
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        // Define os valores gerados no objeto DocumentoCobranca
        documentoCobrancaObj.setNumeroCodigoBarras(numCodBarras);
        documentoCobrancaObj.setTextoLinhaDigitavel(textoLinhaDigitavel); // <<< Usa a linha digitável real

        return documentoCobrancaObj;
    }

    protected void validarDadosComuns() { // Nome mantido
        if (documentoCobrancaObj.getDadosRecebedor() == null) {
            throw new IllegalArgumentException("Informações do Recebedor não foram fornecidas.");
        }
        if (documentoCobrancaObj.getDadosPagador() == null) {
            throw new IllegalArgumentException("Informações do Pagador não foram fornecidas.");
        }
        if (documentoCobrancaObj.getDataLimitePagamento() == null) {
            throw new IllegalArgumentException("Data limite para pagamento não foi informada.");
        }
        if (documentoCobrancaObj.getValorNominal() == null || documentoCobrancaObj.getValorNominal().compareTo(BigDecimal.ZERO) < 0) { // Mudado para permitir zero, mas não negativo
            throw new IllegalArgumentException("Valor nominal do documento é inválido (não pode ser nulo ou negativo).");
        }
    }

    protected abstract void validarDadosEspecificos(); // Nome mantido (método de template)
    protected abstract String gerarCampoLivre(); // Nome mantido (método de template)
}