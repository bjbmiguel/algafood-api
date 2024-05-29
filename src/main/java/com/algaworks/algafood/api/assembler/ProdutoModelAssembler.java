package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoModelAssembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    public ProdutoModel toModel(Produto produto) {
        return mapperConfig.modelMapper().map(produto, ProdutoModel.class);
    }

    public List<ProdutoModel> toCollectionModel(List<Produto> list) {
        return list.stream().map(this::toModel).collect(Collectors.toList());
    }
}
