package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;


    @Autowired
    private FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        var restauranteModel = createModelWithId(restaurante.getId(), restaurante); //Criamos um object Model com self link
        mapperConfig.modelMapper().map(restaurante, restauranteModel);//copiamos os valores from entity to model

        if (algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(links.linkToRestaurantes("restaurantes"));

        }

        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            if (restaurante.podeAtivar()) {

                restauranteModel.add(links.linkToRestauranteAtivacao(restauranteModel.getId(), "ativar"));
            }

            if (restaurante.podeInativar()) {
                restauranteModel.add(links.linkToRestauranteInativacao(restauranteModel.getId(), "inativar"));
            }
        }

        if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
            if (restaurante.podeAbrir()) {

                restauranteModel.add(links.linkToRestauranteAbertura(restauranteModel.getId(), "abrir"));
            }

            if (restaurante.podeFechar()) {
                restauranteModel.add(links.linkToRestauranteFechamento(restauranteModel.getId(), "fechar"));
            }
        }

        if (algaSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    links.linkToCozinha(restaurante.getCozinha().getId()));
        }


        if (algaSecurity.podeConsultarCidades()) {
            if (Objects.nonNull(restauranteModel.getEndereco()) &&
                    Objects.nonNull(restauranteModel.getEndereco().getCidade())) {

                restauranteModel.getEndereco().getCidade().add(
                        links.linkToCidade(restaurante.getEndereco().getCidade().getId()));

            }
        }


        if (algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(links.linkToRestauranteFormasPagamento(restaurante.getId(),
                    "formas-pagamento"));
        }

        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            restauranteModel.add(links.linkToUsuario(restaurante.getId(),
                    "responsaveis"));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        var collection = super.toCollectionModel(entities);

        if (algaSecurity.podeConsultarRestaurantes()) {
              collection.add(links.linkToRestaurantes());
        }
        return  collection;
    }
}