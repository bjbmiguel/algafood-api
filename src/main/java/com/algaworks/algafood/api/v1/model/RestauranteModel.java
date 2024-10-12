package com.algaworks.algafood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Setter
@Getter
@Relation(itemRelation = "restaurante")
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Thai Gourmet")
    private String nome;

    @Schema(example = "12.00")
    private BigDecimal taxaFrete;

    //@JsonView(RestauranteView.Resumo.class) //A propriedade faz parte do resumo...
    private CozinhaModel cozinha;

    private EnderecoModel endereco;
    private Boolean ativo;
    private Boolean aberto;

}