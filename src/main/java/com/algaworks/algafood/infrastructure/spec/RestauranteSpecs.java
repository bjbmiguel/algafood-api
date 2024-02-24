package com.algaworks.algafood.infrastructure.spec;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecs {

    //Retorna um specification de "Restaurante com frete grátis"
    public static Specification<Restaurante> comFreteGratis(){

        return  ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO));
    }

    //Retorna um specification de "Restaurante com nome semelhantes"
    public static Specification<Restaurante> comNomeSemelhante(String nome){

        return  ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
    }
}
