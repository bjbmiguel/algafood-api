package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Grupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrupoInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public Grupo toDomainObject(GrupoInput grupoInput) {
        return mapperConfig.modelMapper().map(grupoInput, Grupo.class);

    }

    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
        mapperConfig.modelMapper().map(grupoInput, grupo);
    }

}