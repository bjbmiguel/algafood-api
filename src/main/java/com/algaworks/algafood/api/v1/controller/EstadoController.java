package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastrarEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/estados")
@RestController
// Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class EstadoController implements EstadoControllerOpenApi {

    @Autowired // Injetamos a dependência
    private CadastrarEstadoService cadastrarEstadoService;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @Autowired
    private EstadoInputDisassembler estadoInputDisassembler;


    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    @CheckSecurity.Estados.PodeConsultar
    public CollectionModel<EstadoModel> listar() {

        List<Estado> todosEstados = cadastrarEstadoService.listar();

        return estadoModelAssembler.toCollectionModel(todosEstados);
    }

    @GetMapping("/{estadoId}")
    @CheckSecurity.Estados.PodeConsultar
    public EstadoModel buscar(@PathVariable Long estadoId) {
        Estado estado = cadastrarEstadoService.hasOrNot(estadoId);

        return estadoModelAssembler.toModel(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.Estados.PodeEditar
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);

        estado = cadastrarEstadoService.salvar(estado);

        return estadoModelAssembler.toModel(estado);
    }

    @PutMapping("/{estadoId}")
    @CheckSecurity.Estados.PodeEditar
    public EstadoModel atualizar(@PathVariable Long estadoId,
                                 @RequestBody @Valid EstadoInput estadoInput) {
        Estado estadoAtual = cadastrarEstadoService.hasOrNot(estadoId);

        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

        estadoAtual = cadastrarEstadoService.salvar(estadoAtual);

        return estadoModelAssembler.toModel(estadoAtual);
    }

    @DeleteMapping(value = "/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Estados.PodeEditar
    public void remover(@PathVariable Long estadoId) {

        cadastrarEstadoService.excluir(estadoId);

    }

}
