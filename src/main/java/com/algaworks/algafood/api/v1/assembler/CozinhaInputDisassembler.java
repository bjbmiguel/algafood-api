package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public Cozinha toDomainObject(CozinhaInput cozinhaInput) {
        return mapperConfig.modelMapper().map(cozinhaInput, Cozinha.class);

    }

    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
        mapperConfig.modelMapper().map(cozinhaInput, cozinha);
    }

}