package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

public interface UsuarioGrupoControllerOpenApi {


    CollectionModel<GrupoModel> listar(

            Long usuarioId);


    ResponseEntity<Void> removerGrupo(

            Long usuarioId,


            Long grupoId);


    ResponseEntity<Void> adicionarGrupo(

            Long usuarioId,


            Long grupoId);

}