package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import org.springframework.util.StringUtils;

import static com.algaworks.algafood.infrastructure.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.spec.RestauranteSpecs.comNomeSemelhante;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy  // A dependência RestauranteRepository será instânciada somente quando for necessário...
    RestauranteRepository restauranteRepository;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        var jpql = "from Restaurante where nome like :nome "
                + "and taxaFrete between :taxaInicial and :taxaFinal";

        return manager.createQuery(jpql, Restaurante.class)
                .setParameter("nome", "%" + nome + "%")
                .setParameter("taxaInicial", taxaFreteInicial)
                .setParameter("taxaFinal", taxaFreteFinal)
                .getResultList();
    }

    public List<Restaurante> findDinamica(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        var jpql = new StringBuilder();
        jpql.append("from Restaurante where 0 = 0 ");

        //Para pegar todos os valores informados pelo user
        var parametros = new HashMap<String, Object>();

        //Este método verifica se um String é null ou vazia
        if (StringUtils.hasLength(nome)) { // Se o nome foi informado concatena...
            jpql.append("and nome like :nome ");
            parametros.put("nome", "%" + nome + "%");
        }
        if (taxaFreteInicial != null) {
            jpql.append("and taxaFrete >= :taxaInicial ");
            parametros.put("taxaInicial", taxaFreteInicial);
        }

        if (taxaFreteFinal != null) {
            jpql.append("and taxaFrete <= :taxaFinal ");
            parametros.put("taxaFinal", taxaFreteFinal);
        }
        TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);

        parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

        return query.getResultList();

    }

    @Override
    public List<Restaurante> findWithCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        //Fabrica para construir elementos de uma consulta...
        CriteriaBuilder builder = manager.getCriteriaBuilder(); // Usamos para obter uma inst de CriteriaBuilder

        //Uma interface responsável por criar uma "CriteriaQuery" e  montar a composição/critérios de uma consulta sql (construtor de clausulas)
        //CriteriaQuery<Restaurante> criteriaQuery = builder.createQuery(Restaurante.class);
        var criteriaQuery = builder.createQuery(Restaurante.class);
        //Root<Restaurante> root = criteriaQuery.from(Restaurante.class); // from Restaurante  | Adicionamos uma claúsula from.
        var root = criteriaQuery.from(Restaurante.class); // from Restaurante  | Adicionamos uma claúsula from.

        //Root = Restaurante
        var predicates = new ArrayList<Predicate>();

        //Filtro...
        if (StringUtils.hasLength(nome)) { // Se o nome foi informado concatena...
            predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
        }

        if (taxaFreteInicial != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
        }

        if (taxaFreteFinal != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0])); //Convertermos uma lista de predicates para Array...
        var typedQuery = manager.createQuery(criteriaQuery); //optimizando... usando var
        //TypedQuery<Restaurante> typedQuery = manager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {

        return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
    }


}