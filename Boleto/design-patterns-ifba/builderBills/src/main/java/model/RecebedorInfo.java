package model;

public class RecebedorInfo {

    private String nomeCompleto;
    private String identificacaoOficial; // Ex: CPF/CNPJ
    private String logradouroCompleto;

    public RecebedorInfo(String nomeRec, String idOficialRec, String logradouroRec) {
        this.nomeCompleto = nomeRec;
        this.identificacaoOficial = idOficialRec;
        this.logradouroCompleto = logradouroRec;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getLogradouroCompleto() {
        return logradouroCompleto;
    }

    public String getIdentificacaoOficial() {
        return identificacaoOficial;
    }
}