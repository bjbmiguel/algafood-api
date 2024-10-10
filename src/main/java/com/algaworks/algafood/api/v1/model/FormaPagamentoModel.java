package com.algaworks.algafood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class FormaPagamentoModel extends RepresentationModel<FormaPagamentoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Cartão de crédito")
    private String descricao;
}
