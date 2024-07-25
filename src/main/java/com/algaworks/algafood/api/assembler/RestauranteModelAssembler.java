package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;


import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;


    @Autowired
    private FactoryLinks links;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        var restauranteModel = createModelWithId(restaurante.getId(), restaurante); //Criamos um object Model com self link
        mapperConfig.modelMapper().map(restaurante, restauranteModel);//copiamos os valores from entity to model
        return restauranteModel;
    }

    public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
        return restaurantes.stream()
                .map(restaurante -> toModel(restaurante))
                .collect(Collectors.toList());
    }

}