package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Permissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissaoModelAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    //Entity --> Model/ListModel
    public PermissaoModel toModel(Permissao permissao) {

        return mapperConfig.modelMapper().map(permissao, PermissaoModel.class);
    }

    //List Entity --> List Model
    public List<PermissaoModel> toCollectionModel(Collection<Permissao> permissaoList) {
        return permissaoList.stream()
                .map(permissao -> toModel(permissao))
                .collect(Collectors.toList());
    }



}