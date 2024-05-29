package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return mapperConfig.modelMapper().map(restauranteInput, Restaurante.class);
    }

    //copyProperties from InputModel to Entity
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
        // Para evitar org.hibernate.HibernateException: identifier of an instance of
        // com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
        restaurante.setCozinha(new Cozinha());

        if (restaurante.getEndereco() != null) {
            restaurante.getEndereco().setCidade(new Cidade());
        }

        mapperConfig.modelMapper().map(restauranteInput, restaurante);
    }

}