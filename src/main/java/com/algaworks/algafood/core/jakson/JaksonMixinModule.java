package com.algaworks.algafood.core.jakson;

import com.algaworks.algafood.api.v1.model.mixin.CidadeMixin;
import com.algaworks.algafood.api.v1.model.mixin.CozinhaMixin;
import com.algaworks.algafood.api.v1.model.mixin.RestauranteMixin;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JaksonMixinModule extends SimpleModule {

    public JaksonMixinModule() {
        //Vinculamos Restaurante com RestauranteMixin, ou seja, RestauranteMixin é uma configuração de Restayrante
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
        setMixInAnnotation(Cidade.class, CidadeMixin.class);
        setMixInAnnotation(Cozinha.class, CozinhaMixin.class);

    }
}
