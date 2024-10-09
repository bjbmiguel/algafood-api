package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.UsuarioModel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

    CollectionModel<UsuarioModel> listar(Long restauranteId);

    ResponseEntity<Void> removerResponsavel(Long restauranteId,Long usuarioId);

    ResponseEntity<Void> adicionarResponsavel(Long restauranteId,Long usuarioId);

}