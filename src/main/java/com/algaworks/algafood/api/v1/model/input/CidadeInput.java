package com.algaworks.algafood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CidadeInput {


    @NotBlank
    @Schema(example = "1")
    private String nome;

    @Valid
    @NotNull
    private EstadoIdInput estado;

}