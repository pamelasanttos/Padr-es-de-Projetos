package model;

public class PagadorInfo {

    private String nomeCompleto;
    private String identificacaoOficial; // Ex: CPF/CNPJ
    private String logradouroCompleto;

    public PagadorInfo(String nomePag, String idOficialPag, String logradouroPag) {
        this.nomeCompleto = nomePag;
        this.identificacaoOficial = idOficialPag;
        this.logradouroCompleto = logradouroPag;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getIdentificacaoOficial() {
        return identificacaoOficial;
    }

    public String getLogradouroCompleto() {
        return logradouroCompleto;
    }
}