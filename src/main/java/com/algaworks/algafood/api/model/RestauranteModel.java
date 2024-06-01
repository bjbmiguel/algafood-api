package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteModel {

    @JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private Long id;

    @JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private String nome;

    @JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private BigDecimal taxaFrete;

    @JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private CozinhaModel cozinha;

    private EnderecoModel endereco;
    private Boolean ativo;
    private Boolean aberto;

}