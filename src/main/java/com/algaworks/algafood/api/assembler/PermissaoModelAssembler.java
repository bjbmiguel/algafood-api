package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Permissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PermissaoModelAssembler implements RepresentationModelAssembler<Permissao, PermissaoModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;

    @Autowired
    FactoryLinks links;

    //Entity --> Model/ListModel
    public PermissaoModel toModel(Permissao permissao) {
        return mapperConfig.modelMapper().map(permissao, PermissaoModel.class);
    }

    //List Entity --> List Model

    @Override
    public CollectionModel<PermissaoModel> toCollectionModel(Iterable<? extends Permissao> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(links.linkToPermissoes());
    }



}