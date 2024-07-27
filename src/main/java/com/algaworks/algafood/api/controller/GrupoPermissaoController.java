package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.assembler.PermissaoInputDisassembler;
import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.GrupoPermissao;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastrarGrupoPermissaoService;
import com.algaworks.algafood.domain.service.CadastrarGrupoService;
import com.algaworks.algafood.domain.service.CadastrarPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    @Autowired
    CadastrarGrupoService cadastrarGrupoService;

    @Autowired
    GrupoInputDisassembler grupoInputDisassembler;

    @Autowired
    GrupoModelAssembler grupoModelAssembler;

    @Autowired
    PermissaoInputDisassembler permissaoInputDisassembler;

    @Autowired
    PermissaoModelAssembler permissaoModelAssembler;

    @Autowired
    CadastrarPermissaoService cadastrarPermissaoService;

    @Autowired
    CadastrarGrupoPermissaoService cadastrarGrupoPermissaoService;

    @Autowired
    FactoryLinks links;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {

        Grupo grupo = cadastrarGrupoService.findById(grupoId);


        CollectionModel<PermissaoModel> permissoesModel
                = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
                .removeLinks()
                .add(links.linkToGrupoPermissoes(grupoId))
                .add(links.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

        permissoesModel.getContent().forEach(permissaoModel -> {
            permissaoModel.add(links.linkToGrupoPermissaoDesassociacao(
                    grupoId, permissaoModel.getId(), "desassociar"));
        });

        return permissoesModel;
    }

    @PutMapping(path = "/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {

        Grupo grupo = cadastrarGrupoService.findById(grupoId);
        Permissao permissao = cadastrarPermissaoService.findById(permissaoId);

        cadastrarGrupoService.adicionarPermissao(grupo, permissao);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {

        cadastrarGrupoService.checkIfExistById(grupoId);

        GrupoPermissao grupoPermissao = cadastrarGrupoPermissaoService.findById(grupoId, permissaoId);

        cadastrarGrupoService.removerPermissao(grupoPermissao.getGrupo(), grupoPermissao.getPermissao());

        return ResponseEntity.noContent().build();

    }
}
