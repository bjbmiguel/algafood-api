package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.assembler.PermissaoInputDisassembler;
import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.GrupoPermissao;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastrarGrupoPermissaoService;
import com.algaworks.algafood.domain.service.CadastrarGrupoService;
import com.algaworks.algafood.domain.service.CadastrarPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

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

    @GetMapping
    public List<PermissaoModel> listar(@PathVariable Long grupoId) {

        Grupo grupo = cadastrarGrupoService.findById(grupoId);

        return permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());
    }

    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {

        Grupo grupo = cadastrarGrupoService.findById(grupoId);
        Permissao permissao = cadastrarPermissaoService.findById(permissaoId);

        cadastrarGrupoService.adicionarPermissao(grupo, permissao);

    }

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {

        cadastrarGrupoService.checkIfExistById(grupoId);

        GrupoPermissao grupoPermissao = cadastrarGrupoPermissaoService.findById(grupoId, permissaoId);

        cadastrarGrupoService.removerPermissao(grupoPermissao.getGrupo(), grupoPermissao.getPermissao());

    }
}
