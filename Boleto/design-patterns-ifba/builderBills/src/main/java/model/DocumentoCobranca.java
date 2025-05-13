package model;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentoCobranca {

    private String codigoBancoInstituicao;
    private RecebedorInfo dadosRecebedor;
    private PagadorInfo dadosPagador;
    private Date dataEmissao;
    private Date dataLimitePagamento;
    private String idDocumentoOriginador;
    private String textoInstrucoes;
    private String descricaoLocalPagamento;
    private BigDecimal valorNominal;
    private BigDecimal valorAbatimento; // Desconto
    private BigDecimal valorPenalidade; // Multa
    private BigDecimal valorAcrescimos; // Juros
    private String codigoAgencia;
    private String codigoConta;
    private String idCarteiraCobranca;
    private String idNossoNumero;
    private String numeroCodigoBarras;
    private String textoLinhaDigitavel;

    public String getCodigoBancoInstituicao() {
        return codigoBancoInstituicao;
    }

    public void setCodigoBancoInstituicao(String codBanco) {
        this.codigoBancoInstituicao = codBanco;
    }

    public RecebedorInfo getDadosRecebedor() {
        return dadosRecebedor;
    }

    public void setDadosRecebedor(RecebedorInfo recInfo) {
        this.dadosRecebedor = recInfo;
    }

    public PagadorInfo getDadosPagador() {
        return dadosPagador;
    }

    public void setDadosPagador(PagadorInfo pagInfo) {
        this.dadosPagador = pagInfo;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dtEmissao) {
        this.dataEmissao = dtEmissao;
    }

    public Date getDataLimitePagamento() {
        return dataLimitePagamento;
    }

    public void setDataLimitePagamento(Date dtLimite) {
        this.dataLimitePagamento = dtLimite;
    }

    public String getIdDocumentoOriginador() {
        return idDocumentoOriginador;
    }

    public void setIdDocumentoOriginador(String idDocOrig) {
        this.idDocumentoOriginador = idDocOrig;
    }

    public String getTextoInstrucoes() {
        return textoInstrucoes;
    }

    public void setTextoInstrucoes(String instrucoesPag) {
        this.textoInstrucoes = instrucoesPag;
    }

    public String getDescricaoLocalPagamento() {
        return descricaoLocalPagamento;
    }

    public void setDescricaoLocalPagamento(String localPag) {
        this.descricaoLocalPagamento = localPag;
    }

    public BigDecimal getValorNominal() {
        return valorNominal;
    }

    public void setValorNominal(BigDecimal valNominal) {
        this.valorNominal = valNominal;
    }

    public BigDecimal getValorAbatimento() {
        return valorAbatimento;
    }

    public void setValorAbatimento(BigDecimal valAbatimento) {
        this.valorAbatimento = valAbatimento;
    }

    public BigDecimal getValorPenalidade() {
        return valorPenalidade;
    }

    public void setValorPenalidade(BigDecimal valPenalidade) {
        this.valorPenalidade = valPenalidade;
    }

    public BigDecimal getValorAcrescimos() {
        return valorAcrescimos;
    }

    public void setValorAcrescimos(BigDecimal valAcrescimos) {
        this.valorAcrescimos = valAcrescimos;
    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codAgencia) {
        this.codigoAgencia = codAgencia;
    }

    public String getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(String codConta) {
        this.codigoConta = codConta;
    }

    public String getIdCarteiraCobranca() {
        return idCarteiraCobranca;
    }

    public void setIdCarteiraCobranca(String idCart) {
        this.idCarteiraCobranca = idCart;
    }

    public String getIdNossoNumero() {
        return idNossoNumero;
    }

    public void setIdNossoNumero(String nosNum) {
        this.idNossoNumero = nosNum;
    }

    public String getNumeroCodigoBarras() {
        return numeroCodigoBarras;
    }

    public void setNumeroCodigoBarras(String numCodBarras) {
        this.numeroCodigoBarras = numCodBarras;
    }

    public String getTextoLinhaDigitavel() {
        return textoLinhaDigitavel;
    }

    public void setTextoLinhaDigitavel(String txtLinhaDig) {
        this.textoLinhaDigitavel = txtLinhaDig;
    }
}