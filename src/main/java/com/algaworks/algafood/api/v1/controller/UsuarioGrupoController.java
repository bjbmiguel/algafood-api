package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.model.UsuarioGrupo;
import com.algaworks.algafood.domain.service.CadastrarGrupoService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioGrupoService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/usuarios/{usuarioId}/grupos")
@RestController
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    @Autowired
    CadastrarGrupoService cadastrarGrupoService;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    GrupoModelAssembler grupoModelAssembler;

    @Autowired
    GrupoInputDisassembler grupoInputDisassembler;

    @Autowired
    CadastrarUsuarioGrupoService cadastrarUsuarioGrupoService;

    @Autowired
    FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {

        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);
        CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
                .removeLinks();


        if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
            gruposModel.add(links.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

            gruposModel.getContent().forEach(grupoModel -> {
                grupoModel.add(links.linkToUsuarioGrupoDesassociacao(
                        usuarioId, grupoModel.getId(), "desassociar"));
            });

        }



        return gruposModel;
    }


    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){

        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);
        Grupo grupo = cadastrarGrupoService.findById(grupoId);

        cadastrarUsuarioService.adicionarGrupo(usuario, grupo);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){

        cadastrarUsuarioService.checkIfExistsById(usuarioId);
        UsuarioGrupo usuarioGrupo = cadastrarUsuarioGrupoService.findById(usuarioId, grupoId);
        cadastrarUsuarioService.removerGrupo(usuarioGrupo.getUsuario(), usuarioGrupo.getGrupo());

        return ResponseEntity.noContent().build();
    }
}
