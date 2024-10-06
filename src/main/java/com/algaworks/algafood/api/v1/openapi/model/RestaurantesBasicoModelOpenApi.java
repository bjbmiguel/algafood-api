package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;


@Data
public class RestaurantesBasicoModelOpenApi {

    private RestaurantesEmbeddedModelOpenApi _embedded;
    private Links _links;


    @Data
    public class RestaurantesEmbeddedModelOpenApi {

        private List<RestauranteBasicoModel> restaurantes;

    }

}
