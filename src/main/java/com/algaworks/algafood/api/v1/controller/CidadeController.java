package com.algaworks.algafood.api.v1.controller;


import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CadastrarCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// Definimos a classe como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
@RequestMapping("/v1/cidades")
@RestController
public class CidadeController implements CidadeControllerOpenApi {

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Mapeamos as requ HTTP do tipo GET para este método
    @CheckSecurity.Cidades.PodeConsultar
    public CollectionModel<CidadeModel> listar() {
        var todasCidades = cadastrarCidadeService.listar();
        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Cidades.PodeConsultar
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        var cidade = cadastrarCidadeService.hasOrNot(cidadeId);
        return cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.Cidades.PodeEditar
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastrarCidadeService.salvar(cidade);

            //Adicionamos o id na criação da URI do novo recurso
            ResourceUriHelper.addUriInResponseHeader(cidade.getId());

            return cidadeModelAssembler.toModel(cidade);
        } catch (EstadoNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Cidades.PodeEditar
    public CidadeModel atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = cadastrarCidadeService.hasOrNot(cidadeId);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastrarCidadeService.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }


    @DeleteMapping(value = "/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Cidades.PodeEditar
    public ResponseEntity<Void> remover(@PathVariable Long cidadeId) {

        cadastrarCidadeService.excluir(cidadeId);

        return ResponseEntity.noContent().build();
    }


}
