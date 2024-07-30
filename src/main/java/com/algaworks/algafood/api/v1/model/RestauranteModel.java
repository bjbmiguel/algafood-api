package com.algaworks.algafood.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Setter
@Getter
@Relation(itemRelation = "restaurante")
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    @ApiModelProperty(example = "1")
    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private Long id;

    @ApiModelProperty(example = "Thai Gourmet")
    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNomes.class}) //A propriedade faz parte do resumo...
    private String nome;

    @ApiModelProperty(example = "12.00")
    //@JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private BigDecimal taxaFrete;

    //@JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private CozinhaModel cozinha;

    private EnderecoModel endereco;
    private Boolean ativo;
    private Boolean aberto;

}