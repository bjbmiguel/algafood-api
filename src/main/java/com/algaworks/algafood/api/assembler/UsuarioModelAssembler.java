package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioModelAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public UsuarioModel toModel(Usuario usuario) {
        return mapperConfig.modelMapper().map(usuario, UsuarioModel.class);
    }

    public List<UsuarioModel> toCollectionModel(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(usuario -> toModel(usuario))
                .collect(Collectors.toList());
    }



}