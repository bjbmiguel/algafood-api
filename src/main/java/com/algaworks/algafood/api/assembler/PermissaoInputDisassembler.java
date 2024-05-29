package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Permissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissaoInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    //InputModel --> Entity
    public Permissao toDomainObject(PermissaoInput permissaoInput) {
        return mapperConfig.modelMapper().map(permissaoInput, Permissao.class);

    }

    public void copyToDomainObject(PermissaoInput permissaoInput, Permissao permissao) {
        mapperConfig.modelMapper().map(permissaoInput, permissao);
    }

}