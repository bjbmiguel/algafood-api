package com.algaworks.algafood.api.exceptionhanlder;

import lombok.Getter;

import java.security.PrivateKey;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    RECURSO_NAO_ENCONTRADA("/recurso-nao-encontrada", "Entidade não encontrada"),
    PROPRIEDADE_NAO_ENCONTRADA("/propriedade-nao-encontrada", "Propriedade não encontrada"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),

    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
    //ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
  //  ERRO_NEGOCIO ("/requisicao-errada", "Requisição errada");

    private String title;
    private String uri;

    ProblemType(String path, String title) {

        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }
}
