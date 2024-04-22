package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastrarGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/grupos")
@RestController
// Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class GrupoController {

    @Autowired // Injetamos a dependência
    private CadastrarGrupoService cadastrarGrupoService;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;


    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<GrupoModel> listar() {

        List<Grupo> todosGrupos = cadastrarGrupoService.listar();

        return grupoModelAssembler.toCollectionModel(todosGrupos);
    }

    @GetMapping("/{grupoId}")
    public GrupoModel buscar(@PathVariable Long grupoId) {
        Grupo grupo = cadastrarGrupoService.hasOrNot(grupoId);

        return grupoModelAssembler.toModel(grupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

        grupo = cadastrarGrupoService.salvar(grupo);

        return grupoModelAssembler.toModel(grupo);
    }

    @PutMapping("/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId,
                                 @RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupoAtual = cadastrarGrupoService.hasOrNot(grupoId);

        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);

        grupoAtual = cadastrarGrupoService.salvar(grupoAtual);

        return grupoModelAssembler.toModel(grupoAtual);
    }

    @DeleteMapping(value = "/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {

        cadastrarGrupoService.excluir(grupoId);

    }

}
