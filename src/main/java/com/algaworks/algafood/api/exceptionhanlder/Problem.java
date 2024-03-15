package com.algaworks.algafood.api.exceptionhanlder;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder //usamos para construir objectos de forma mais fluente...
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  //Somente campos não null serão incluídos...
public class Problem {

    private Integer status; //Código do status Http da resposta
    private String type; // Uma URI (URL) que especifica o tipo do problema e a solução
    private String title; // Uma descrição do tipo do problema, mas legível para humano
    private String detail; //Uma descrição mais detalhada sobre a ocurrência do problema

}
