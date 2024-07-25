package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;


    @Autowired
    private FactoryLinks links;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        var restauranteModel = createModelWithId(restaurante.getId(), restaurante); //Criamos um object Model com self link
        mapperConfig.modelMapper().map(restaurante, restauranteModel);//copiamos os valores from entity to model

        restauranteModel.add(links.linkToRestaurantes("restaurantes"));

        if(restaurante.podeAtivar()){

            restauranteModel.add(links.linkToAtivarRestaurante(restauranteModel.getId(), "ativar"));
        }

        if(restaurante.podeInativar()){
            restauranteModel.add(links.linkToInativarRestaurante(restauranteModel.getId(), "inativar"));
        }

        if(restaurante.podeAbrir()){

            restauranteModel.add(links.linkToAbrirRestaurante(restauranteModel.getId(), "abrir"));
        }

        if(restaurante.podeFechar()){
            restauranteModel.add(links.linkToFecharRestaurante(restauranteModel.getId(), "fechar"));
        }

        restauranteModel.getCozinha().add(
                links.linkToCozinha(restaurante.getCozinha().getId()));

        restauranteModel.getEndereco().getCidade().add(
                links.linkToCidade(restaurante.getEndereco().getCidade().getId()));

        restauranteModel.add(links.linkToRestauranteFormasPagamento(restaurante.getId(),
                "formas-pagamento"));

        restauranteModel.add(links.linkToResponsaveisRestaurante(restaurante.getId(),
                "responsaveis"));


        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities)
                .add(links.linkToRestaurantes());
    }
}