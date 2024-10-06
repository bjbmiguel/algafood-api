package com.algaworks.algafood.api.v1.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Setter
@Getter
@Relation(itemRelation = "restaurante")
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private Long id;

    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private String nome;

    //@JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private BigDecimal taxaFrete;

    //@JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private CozinhaModel cozinha;

    private EnderecoModel endereco;
    private Boolean ativo;
    private Boolean aberto;

}