package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestauranteRepository  extends CustomJpaRepository<Restaurante, Long> , RestauranteRepositoryQueries,
        JpaSpecificationExecutor<Restaurante> {

    @Query("from Restaurante r inner join fetch r.cozinha")
    List<Restaurante> findAll();

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
    List<Restaurante> consultarPorNome(String nome, @Param("id")  Long cozinha);

    Optional<Restaurante> readFirstRestauranteByNomeContaining(String nome);

    //O Top2 é uma palavra reservada...
    List<Restaurante> streamTop2ByNomeContaining(String nome);

    int  countByCozinhaId(Long cozinhaId);



}