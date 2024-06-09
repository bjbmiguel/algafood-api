package com.algaworks.algafood.infrastructure.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import com.algaworks.algafood.domain.model.StatusPedido;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {

        var builder = manager.getCriteriaBuilder(); //Obtermos uma instância de CriteriaBuilder via entityManager
        var query = builder.createQuery(VendaDiaria.class); // Criamos uma instância de CriteriaQuery para um tipo especificado
        var root = query.from(Pedido.class); //Declaramos um root, especificando a clausula from...

        var predicates = new ArrayList<Predicate>();

        var functionConvertTzDataCriacao = builder.function(
                "convert_tz", Date.class, root.get("dataCriacao"),
                builder.literal("+00:00"), builder.literal(timeOffset));



        var functionDateDataCriacao = builder.function(
                "date", Date.class, functionConvertTzDataCriacao);


        var st = root.get("status");

        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")), st);
              //  builder.and(root.get("status")));  //Construimos /projetamos o nosso select

        if(filtro.getRestauranteId() !=null){
            predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
        }

        if(filtro.getDataCriacaoInicio() !=null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
        }

        if(filtro.getDataCriacaoFim() !=null){
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
        }

        predicates.add(st.in(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));

        query.select(selection)
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(functionDateDataCriacao, st);

        return manager.createQuery(query).getResultList(); //Executamos a query
    }

}