package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.api.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CadastrarCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Definimos a classe como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
@RequestMapping("/cidades")
@RestController
public class CidadeController implements CidadeControllerOpenApi {

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Mapeamos as requ HTTP do tipo GET para este método
    public CollectionModel<CidadeModel> listar() {
        var todasCidades = cadastrarCidadeService.listar();
        var cidadesModel = cidadeModelAssembler.toCollectionModel(todasCidades);


        cidadesModel.forEach(cidadeModel -> {
            cidadeModel.add(linkTo(methodOn(CidadeController.class)
                    .buscar(cidadeModel.getId())).withSelfRel());

            cidadeModel.add(linkTo(methodOn(CidadeController.class)
                    .listar()).withRel("cidades"));

            cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
                    .buscar(cidadeModel.getEstado().getId())).withSelfRel());
        });


        CollectionModel<CidadeModel> cidadesCollectionModel = CollectionModel.of(cidadesModel);

        cidadesCollectionModel.add(linkTo(CidadeController.class).withSelfRel());

        return cidadesCollectionModel;
    }

    @GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel buscar(@PathVariable Long cidadeId) {

        var cidade = cadastrarCidadeService.hasOrNot(cidadeId);

        var cidadeModel = cidadeModelAssembler.toModel(cidade);

        cidadeModel.add(linkTo(methodOn(CidadeController.class)
                .buscar(cidadeModel.getId())).withSelfRel());

        cidadeModel.add(linkTo(methodOn(CidadeController.class)
                .listar()).withRel("cidades"));


        cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
                .buscar(cidadeModel.getEstado().getId())).withSelfRel());

        return cidadeModel;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
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
    public void remover(@PathVariable Long cidadeId) {

        cadastrarCidadeService.excluir(cidadeId);

    }


}
