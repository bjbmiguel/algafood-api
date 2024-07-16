package com.algaworks.algafood.api.exceptionhanlder;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Builder //usamos para construir objectos de forma mais fluente...
@Getter
@ApiModel("Problema") // Descricçao da modelo na documentação
@JsonInclude(JsonInclude.Include.NON_NULL)  //Somente campos não null serão incluídos...
public class Problem {


    @ApiModelProperty(example = "400", position = 1)
    private Integer status; //Código do status Http da resposta

    @ApiModelProperty(example = "https://algafood.com.br/dados-invalidos", position = 10)
    private String type; // Uma URI (URL) que especifica o tipo do problema e a solução

    @ApiModelProperty(example = "Dados inválidos", position = 15)
    private String title; // Uma descrição do tipo do problema, mas legível para humano

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
            position = 20)
    private String detail; //Uma descrição mais detalhada sobre a ocurrência do problema

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
            position = 25)
    private String userMessage;

    @ApiModelProperty(example = "2019-12-01T18:09:02.70844Z", position = 5)
    private OffsetDateTime timesTamp;


    @ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)",
            position = 30)
    private List<Object> objects;


    @ApiModel("ObjetoProblema")
    @Getter
    @Builder
    public  static class Object {

        private String name;
        private String userMessage;

    }

}
