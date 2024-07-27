package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.controller.ProdutoRestauranteController;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;

    @Autowired
    private FactoryLinks links;

    public ProdutoModelAssembler() {
        super(ProdutoRestauranteController.class, ProdutoModel.class);
    }


    //Recurso único
    public ProdutoModel toModel(Produto produto) {
        //Criamos um object Model com self link... o método createModelWithId recebe um varArg (...)
        //api.algafood.local:8080/restaurantes/1/produtos/1
        var  produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());

        mapperConfig.modelMapper().map(produto, produtoModel);

        produtoModel.add(links.linkToProduto(produto.getRestauranteId(), null, "produtos"));
        produtoModel.add(links.linkToFotoProduto(produto.getRestauranteId(), produto.getId(), "foto"));

        return produtoModel;
    }


}
