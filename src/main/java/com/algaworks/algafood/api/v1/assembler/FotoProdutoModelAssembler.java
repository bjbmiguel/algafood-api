package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    public FotoProdutoModelAssembler() {
        super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
    }

    public FotoProdutoModel toModel(FotoProduto foto) {
        var fotoProdutoModel = modelMapper.map(foto, FotoProdutoModel.class);

        // Quem pode consultar restaurantes, também pode consultar os produtos e fotos
        if (algaSecurity.podeConsultarRestaurantes()) {

            fotoProdutoModel.add(links.linkToFotoProduto(
                    foto.getRestauranteId(), foto.getProduto().getId()));

            fotoProdutoModel.add(links.linkToProduto(
                    foto.getRestauranteId(), foto.getProduto().getId(), "produto"));
        }



        return fotoProdutoModel;
    }

}