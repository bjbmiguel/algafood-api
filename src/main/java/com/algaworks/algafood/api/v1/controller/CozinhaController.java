package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
// Esta anotação define a classe num controlador de modos que ela possa lidar com requisições HTTP e devolver respostas...
@RequestMapping("/v1/cozinhas")
// Esta anotação é usada para mapear as req HTTP feitas neste controller, ou seja, todas as reqs. /cozinhas vão cair aqui...
public class CozinhaController implements CozinhaControllerOpenApi {

    //TODO analisar o comportamento do usdo da anotação  @GetMapping sem argumento em dois métodos
    //TODO anaisar o comportamento do uso da anotação @RequestMapping directamente nos métodos

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    CozinhaModelAssembler cozinhaModelAssembler;

    @Autowired
    CozinhaInputDisassembler cozinhaInputDisassembler;

    @Autowired
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {

        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

        PagedModel<CozinhaModel> cozinhasPagedModel = pagedResourcesAssembler
                .toModel(cozinhasPage, cozinhaModelAssembler);

        return cozinhasPagedModel;
    }

    @GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        return cozinhaModelAssembler.toModel(cadastroCozinhaService.hasOrNot(cozinhaId));
    }

    @GetMapping(path = "/primeiro", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<CozinhaModel> buscarPrimeiro() {

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.buscarPrimeiro().get()));
    }

    @GetMapping(value = "/por-nome")
    //--> nome --> nome - Retorna uma lista
    public CollectionModel<CozinhaModel> buscarPorNome(String nome) { //o nome virá por "query String"

        return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findTodasByNomeContaining(nome));
    }

    @GetMapping(path = "/unico-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    //--> nome --> nome Retorna uma única instância
    public Optional<CozinhaModel> buscarPorNomeUnico(String nome) { //o nome virá por "query String"

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.findByNome(nome).get()));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) { //Anotamos o parâmetro "cozinha"
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
    }

    @PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CozinhaModel atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {

        Cozinha cozinhaAtual = cadastroCozinhaService.hasOrNot(cozinhaId);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        // BeanUtils.copyProperties(cozinhaInput, cozinhaAtual, "id");
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));

    }


    @DeleteMapping(path = "/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId) {

        cadastroCozinhaService.excluir(cozinhaId);

    }


}


