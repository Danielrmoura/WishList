package com.luizalabs.whitelist.enumeration;

public enum ResponseHeadersEnum {

    FULL_COUNT("Total da Coleção"),
    PAGE_SIZE("Tamanho da página que será exibida no front"),
    PAGE_LIST("Lista que será exibida");

    private final String descricao;

    ResponseHeadersEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
