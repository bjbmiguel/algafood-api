package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Grupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrupoModelAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    //Entity --> Model
    public GrupoModel toModel(Grupo grupo) {

        return mapperConfig.modelMapper().map(grupo, GrupoModel.class);
    }

    //List Entity --> List Model
    public List<GrupoModel> toCollectionModel(List<Grupo> grupos) {
        return grupos.stream()
                .map(grupo -> toModel(grupo))
                .collect(Collectors.toList());
    }



}