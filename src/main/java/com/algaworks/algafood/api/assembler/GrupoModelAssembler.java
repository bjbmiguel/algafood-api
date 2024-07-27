package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.controller.GrupoController;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Grupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrupoModelAssembler extends RepresentationModelAssemblerSupport<Grupo, GrupoModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;

    @Autowired
    private FactoryLinks algaLinks;

    public GrupoModelAssembler() {
        super(GrupoController.class, GrupoModel.class);
    }

    //Entity --> Model
    public GrupoModel toModel(Grupo grupo) {

        GrupoModel grupoModel = createModelWithId(grupo.getId(), grupo);
        mapperConfig.modelMapper().map(grupo, grupoModel);

        grupoModel.add(algaLinks.linkToGrupos("grupos"));

        grupoModel.add(algaLinks.linkToGrupoPermissoes(grupo.getId(), "permissoes"));

        return grupoModel;
    }


    @Override
    public CollectionModel<GrupoModel> toCollectionModel(Iterable<? extends Grupo> entities) {
        return super.toCollectionModel(entities)
                .add(algaLinks.linkToGrupos());
    }



}