package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.RestauranteUsuarioId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteUsuarioRepository extends CustomJpaRepository<RestauranteUsuario, RestauranteUsuarioId>{

    Optional<RestauranteUsuario> findById(RestauranteUsuarioId restauranteUsuarioId);

}
