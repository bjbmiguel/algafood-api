package com.algaworks.algafood.api.exceptionhanlder;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Builder //usamos para construir objectos de forma mais fluente...
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  //Somente campos não null serão incluídos...
@Schema(name = "Problema")
public class Problem {

    @Schema(example = "400")
    private Integer status; //Código do status Http da resposta

    @Schema(example = "https://algafood.com.br/dados-invalidos")
    private String type; // Uma URI (URL) que especifica o tipo do problema e a solução

    @Schema(example = "Dados inválidos")
    private String title; // Uma descrição do tipo do problema, mas legível para humano

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String detail; //Uma descrição mais detalhada sobre a ocurrência do problema

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String userMessage;

    @Schema(example = "2022-07-15T11:21:50.902245498Z")
    private OffsetDateTime timesTamp;

    @Schema(description = "Lista de objetos ou campos que geraram o erro")
    private List<Object> objects;



    @Getter
    @Builder
    @Schema(name = "ObjetoProblema")
    public  static class Object {

        @Schema(example = "preco")
        private String name;

        @Schema(example = "O preço é inválido")
        private String userMessage;

    }

}
