package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.PermissaoModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

public interface GrupoPermissaoControllerOpenApi {

    CollectionModel<PermissaoModel> listar(Long grupoId);

    ResponseEntity<Void> removerPermissao(Long grupoId,Long permissaoId);

    ResponseEntity<Void> adicionarPermissao(Long grupoId,Long permissaoId);

}
