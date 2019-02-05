package io.github.leobugoni.cep.model;

import javax.validation.constraints.Size;

public class Endereco {

    public String bairro;

    @Size(min = 8, max = 8)
    public Integer cep;

    public String cidade;

    public String rua;

    @Size(min = 2, max = 2)
    public String uf;

    public Endereco(final String bairro, final Integer cep, final String cidade, final String rua, final String uf) {
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.rua = rua;
        this.uf = uf;
    }

    public String getBairro() {
        return bairro;
    }

    public Integer getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getRua() {
        return rua;
    }

    public String getUf() {
        return uf;
    }
}
