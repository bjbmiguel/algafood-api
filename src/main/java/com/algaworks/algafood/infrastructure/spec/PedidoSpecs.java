package com.algaworks.algafood.infrastructure.spec;

import javax.persistence.criteria.Predicate;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class PedidoSpecs {

    //Retorna um specification de "Pedido" que filtra várias propriedades especificadas em PedidoFilter
    public static Specification<Pedido> usandoFiltro(PedidoFilter pedidoFilter) {
        //root = Pedido
        return (root, query, builder) -> {

            //Evitando a exceção: query specified join fetching, but the owner of the fetched association was not present in the select list
            if(Pedido.class.equals(query.getResultType())){

                //Resolvendo o problema do N+1
                root.fetch("restaurante").fetch("cozinha"); //O restaurante tem cozinha...
                root.fetch("cliente");
            }


            var predicates = new ArrayList<Predicate>();

            if (pedidoFilter.getClienteId() != null) {//Se tem um cliente no filter...

                //Adicionamos um predicate
                predicates.add(builder.equal(root.get("cliente"), pedidoFilter.getClienteId()));
            }

            if (pedidoFilter.getRestauranteId() != null) {//Se tem um restaurante no filter...

                //Adicionamos um predicate  | restaurante=pedidoFilter.getRestauranteId()
                predicates.add(builder.equal(root.get("restaurante"), pedidoFilter.getRestauranteId()));
            }

            if (pedidoFilter.getDataCriacaoInicio() != null) {//Se tem um dataCriacaoInicio no filter...
                //Adicionamos um predicate  | dataCriacao>=getDataCriacaoInicio
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoInicio()));
            }

            if (pedidoFilter.getDataCriacaoFim() != null) {//Se tem um dataCriacaoFim no filter...
                //Adicionamos um predicate  | dataCriacao>=getDataCriacaoFim
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), pedidoFilter.getDataCriacaoFim()));
            }

            return builder.and(predicates.toArray(new Predicate[0])); //convertemos uma ArrayList em um Array...
        };

    }


}
