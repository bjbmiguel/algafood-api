package com.algaworks.algafood.api.exceptionhanlder;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível."),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrada", "Entidade não encontrada."),
    PROPRIEDADE_NAO_ENCONTRADA("/propriedade-nao-encontrada", "Propriedade não encontrada"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de Sistema"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String title;
    private String uri;

    ProblemType(String path, String title) {

        this.uri = "https://algafood.com.br" + path;
        this.title = title;
    }
}
