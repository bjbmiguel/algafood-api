package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;


import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public RestauranteModel toModel(Restaurante restaurante) {
        return mapperConfig.modelMapper().map(restaurante, RestauranteModel.class);
    }

    public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
        return restaurantes.stream()
                .map(restaurante -> toModel(restaurante))
                .collect(Collectors.toList());
    }



}