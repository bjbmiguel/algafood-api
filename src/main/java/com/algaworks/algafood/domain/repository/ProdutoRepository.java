package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends CustomJpaRepository<Produto, Long> {

//A query será montada em tempo de execução...
List<Produto> findByRestaurante(Restaurante restaurante);

    @Query("from Produto where restaurante.id = :restaurante and id = :produto")
    Optional<Produto> validateProdutoByIdProdutoAndRestaurante(@Param("restaurante") Long restauranteId,
                               @Param("produto") Long produtoId);

Optional<Produto> findByIdAndRestaurante(Long id, Restaurante restaurante);
//findByEmailAddressAndLastname
}
