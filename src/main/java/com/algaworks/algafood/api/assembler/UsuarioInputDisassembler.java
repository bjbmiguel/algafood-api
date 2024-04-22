package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public Usuario toDomainObject(UsuarioInput usuarioInput) {
        return mapperConfig.modelMapper().map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        mapperConfig.modelMapper().map(usuarioInput, usuario);
    }

}