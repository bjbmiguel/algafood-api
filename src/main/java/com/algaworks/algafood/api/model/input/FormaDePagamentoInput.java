package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FormaDePagamentoInput {

    private Long id;

    @NotNull
    private String descricao;

}
