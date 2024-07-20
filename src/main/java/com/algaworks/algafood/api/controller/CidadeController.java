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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    public List<CidadeModel> listar() {

        List<Cidade> todasCidades = cadastrarCidadeService.listar();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CidadeModel buscar(@PathVariable Long cidadeId) {

        var cidade = cadastrarCidadeService.hasOrNot(cidadeId);

        var cidadeModel = cidadeModelAssembler.toModel(cidade);

        cidadeModel.add(linkTo(CidadeController.class)
                .slash(cidadeModel.getId()).withSelfRel());

//		cidadeModel.add(Link.of("http://api.algafood.local:8080/cidades/1"));

        cidadeModel.add(linkTo(CidadeController.class)
                .withRel("cidades"));

//		cidadeModel.add(Link.of("http://api.algafood.local:8080/cidades", "cidades"));

        cidadeModel.getEstado().add(linkTo(EstadoController.class)
                .slash(cidadeModel.getEstado().getId()).withSelfRel());

//		cidadeModel.getEstado().add(Link.of("http://api.algafood.local:8080/estados/1"));


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
