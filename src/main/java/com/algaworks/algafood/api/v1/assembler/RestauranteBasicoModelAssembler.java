package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteBasicoModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;


    @Autowired
    private FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteBasicoModelAssembler() {
        super(RestauranteController.class, RestauranteBasicoModel.class);
    }

    @Override
    public RestauranteBasicoModel toModel(Restaurante restaurante) {

        var restauranteModel = createModelWithId(restaurante.getId(), restaurante); //Criamos um object Model com self link
        mapperConfig.modelMapper().map(restaurante, restauranteModel);//copiamos os valores from entity to model

        if (algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(links.linkToRestaurantes("restaurantes"));
        }

        if (algaSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    links.linkToCozinha(restaurante.getCozinha().getId()));

        }





        return restauranteModel;

    }

    @Override
    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);

        if (algaSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(links.linkToRestaurantes());
        }

        return  collectionModel;
    }

}