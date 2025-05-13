package builder;

import java.math.BigDecimal; // Atualizado
import java.util.Date;

import model.DocumentoCobranca;

public interface ConstrutorDocumentoCobranca {

    ConstrutorDocumentoCobranca comRecebedor(String nomeRec, String docRec, String endRec);
    ConstrutorDocumentoCobranca comPagador(String nomePag, String docPag, String endPag);
    ConstrutorDocumentoCobranca comDadosDocumentoOriginador(String idDocOrig, Date dtEmissao);
    ConstrutorDocumentoCobranca comDataLimitePagamento(Date dtLimite);
    ConstrutorDocumentoCobranca comValorNominal(BigDecimal quantiaNominal);
    ConstrutorDocumentoCobranca comTextoInstrucoes(String instrucoes);
    ConstrutorDocumentoCobranca comDescricaoLocalPagamento(String local);
    ConstrutorDocumentoCobranca comValorAbatimento(BigDecimal valAbatimento);
    ConstrutorDocumentoCobranca comValorPenalidade(BigDecimal valPenalidade);
    ConstrutorDocumentoCobranca comValorAcrescimos(BigDecimal valAcrescimos);
    ConstrutorDocumentoCobranca comCodigoAgencia(String codAgencia);
    ConstrutorDocumentoCobranca comCodigoConta(String codConta);
    ConstrutorDocumentoCobranca comIdCarteiraCobranca(String idCarteira); // Renomeado para corresponder ao campo
    ConstrutorDocumentoCobranca comIdNossoNumero(String idNossoNum);   // Renomeado para corresponder ao campo
    DocumentoCobranca construir(); // Método final renomeado e tipo de retorno atualizado
}