package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
// Esta anotação define a classe num controlador de modos que ela possa lidar com requisições HTTP e devolver respostas...
@RequestMapping("/v1/cozinhas")
@Slf4j
// Esta anotação é usada para mapear as req HTTP feitas neste controller, ou seja, todas as reqs. /cozinhas vão cair aqui...
public
class CozinhaController implements CozinhaControllerOpenApi {

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

    @CheckSecurity.Cozinhas.PodeConsultar//Só lista cozinha quem estiver autenticado
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Consultando cozinhas com páginas de {} de registros", pageable.getPageSize());

        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

        PagedModel<CozinhaModel> cozinhasPagedModel = pagedResourcesAssembler
                .toModel(cozinhasPage, cozinhaModelAssembler);

        return cozinhasPagedModel;
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        return cozinhaModelAssembler.toModel(cadastroCozinhaService.hasOrNot(cozinhaId));
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping(path = "/primeiro", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<CozinhaModel> buscarPrimeiro() {

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.buscarPrimeiro().get()));
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    //--> nome --> nome - Retorna uma lista
    @GetMapping(value = "/por-nome")
    public CollectionModel<CozinhaModel> buscarPorNome(String nome) { //o nome virá por "query String"

        return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findTodasByNomeContaining(nome));
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping(path = "/unico-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    //--> nome --> nome Retorna uma única instância
    public Optional<CozinhaModel> buscarPorNomeUnico(String nome) { //o nome virá por "query String"

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.findByNome(nome).get()));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    @CheckSecurity.Cozinhas.PodeEditar
    @Override
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) { //Anotamos o parâmetro "cozinha"
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
    }

    @PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Cozinhas.PodeEditar
    public CozinhaModel atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {

        Cozinha cozinhaAtual = cadastroCozinhaService.hasOrNot(cozinhaId);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        // BeanUtils.copyProperties(cozinhaInput, cozinhaAtual, "id");
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));

    }


    @DeleteMapping(path = "/{cozinhaId}")
    @CheckSecurity.Cozinhas.PodeEditar
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> remover(@PathVariable Long cozinhaId) {

        cadastroCozinhaService.excluir(cozinhaId);

        return ResponseEntity.noContent().build();

    }


}


