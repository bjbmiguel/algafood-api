package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;
    public RestauranteInput fromDomainModelToInputModel(Restaurante restaurante) {
        return mapperConfig.modelMapper().map(restaurante, RestauranteInput.class);
    }
}
