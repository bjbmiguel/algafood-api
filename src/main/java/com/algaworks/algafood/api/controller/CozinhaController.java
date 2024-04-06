package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/cozinhas")
// Esta anotação é usada para mapear as req HTTP feitas neste controller, ou seja, todas as reqs. /cozinhas vão cair aqui...
@RestController
// Esta anotação define a classe num controlador de modos que ela possa lidar com requisições HTTP e devolver respostas...
public class CozinhaController {

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

    @GetMapping
    public List<CozinhaModel> listar() {

        return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findAll());
    }


    @GetMapping(value = "/{cozinhaId}")
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        return cozinhaModelAssembler.toModel(cadastroCozinhaService.hasOrNot(cozinhaId));
    }

    @GetMapping("/primeiro")
    public Optional<CozinhaModel> buscarPrimeiro() {

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.buscarPrimeiro().get()));
    }

    @GetMapping(value = "/por-nome")
    //--> nome --> nome - Retorna uma lista
    public List<CozinhaModel> buscarPorNome(String nome) { //o nome virá por "query String"

        return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findTodasByNomeContaining(nome));
    }

    @GetMapping(value = "/unico-por-nome")
    //--> nome --> nome Retorna uma única instância
    public Optional<CozinhaModel> buscarPorNomeUnico(String nome) { //o nome virá por "query String"

        return Optional.of(cozinhaModelAssembler.toModel(cozinhaRepository.findByNome(nome).get()));
    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) { //Anotamos o parâmetro "cozinha"
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
    }

    @PutMapping(value = "/{cozinhaId}")
    public CozinhaModel actualizar(@RequestBody @Valid CozinhaInput cozinhaInput, @PathVariable Long cozinhaId) {

        Cozinha cozinhaAtual = cadastroCozinhaService.hasOrNot(cozinhaId);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
       // BeanUtils.copyProperties(cozinhaInput, cozinhaAtual, "id");
        return  cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));

    }



@DeleteMapping(value = "/{cozinhaId}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void remover(@PathVariable Long cozinhaId) {

    cadastroCozinhaService.excluir(cozinhaId);

}


}


