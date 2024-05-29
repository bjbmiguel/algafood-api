package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoInputDisassembler {

    @Autowired
    private ModelMapperConfig mapperConfig;

    //Suggestion of name method, fromInputModelToDomainObject
    public Produto toDomainObject(ProdutoInput produtoInput) {
        return mapperConfig.modelMapper().map(produtoInput, Produto.class);
    }

    public void copyToDomainObject(ProdutoInput produtoInput, Produto produtoAtual){
        mapperConfig.modelMapper().map(produtoInput, produtoAtual);
    }

}
