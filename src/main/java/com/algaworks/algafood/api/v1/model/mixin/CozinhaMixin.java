package com.algaworks.algafood.api.v1.model.mixin;


import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;



import java.util.ArrayList;
import java.util.List;


public class CozinhaMixin {

    @JsonIgnore // vai ignorar a serialização de restaurante quando for serializar a cozinha.
    List<Restaurante> restaurantes  = new ArrayList<>();


}
