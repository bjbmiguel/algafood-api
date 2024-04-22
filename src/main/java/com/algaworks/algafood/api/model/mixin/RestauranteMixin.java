package com.algaworks.algafood.api.model.mixin;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class RestauranteMixin {
    @JsonIgnoreProperties(value = "nome", allowGetters = true) //Vai ignorar o atri nome da Cozinha no proce json -->objec java
    private Cozinha cozinha;

    @JsonIgnore
    List<FormaPagamento> formasPagamento = new ArrayList<>();

    @JsonIgnore
    @Embedded // é uma imcorporação..., i.e, esta classe é uma parte da classe Restaurante.
    private Endereco endereco;

    //@JsonIgnore
    private OffsetDateTime dataCadastro;

    //@JsonIgnore
    private OffsetDateTime dataAtualizacao;

    @JsonIgnore
    List<Produto> produotos = new ArrayList<>();
}
