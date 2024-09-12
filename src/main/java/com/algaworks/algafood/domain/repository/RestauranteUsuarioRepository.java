package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.RestauranteUsuarioId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteUsuarioRepository extends CustomJpaRepository<RestauranteUsuario, RestauranteUsuarioId>{

    Optional<RestauranteUsuario> findById(RestauranteUsuarioId restauranteUsuarioId);

    @Query("FROM RestauranteUsuario r JOIN r.restaurante res JOIN Pedido p ON p.restaurante = res WHERE p.codigo = :codigoPedido AND r.usuario.id = :usuarioId")
    Optional<RestauranteUsuario> findResponsavelByPedido(String codigoPedido, Long usuarioId);



}
