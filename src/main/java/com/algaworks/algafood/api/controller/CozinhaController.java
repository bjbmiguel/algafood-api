package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping
    public List<Cozinha> listar() {

        return cozinhaRepository.findAll();
    }


    @GetMapping(value = "/{cozinhaId}")
    public Cozinha buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        return cadastroCozinhaService.hasOrNot(cozinhaId);
    }

    @GetMapping("/primeiro")
    public Optional<Cozinha> buscarPrimeiro() {

        return cozinhaRepository.buscarPrimeiro();
    }

    @GetMapping(value = "/por-nome")
    //--> nome --> nome - Retorna uma lista
    public List<Cozinha> buscarPorNome(String nome) { //o nome virá por "query String"

        return cozinhaRepository.findTodasByNomeContaining(nome);
    }

    @GetMapping(value = "/unico-por-nome")
    //--> nome --> nome Retorna uma única instância
    public Optional<Cozinha> buscarPorNomeUnico(String nome) { //o nome virá por "query String"

        return cozinhaRepository.findByNome(nome);
    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public Cozinha adicionar(@RequestBody Cozinha cozinha) { //Anotamos o parâmetro "cozinha"
        return cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping(value = "/{cozinhaId}")
    public Cozinha actualizar(@RequestBody Cozinha cozinha, @PathVariable Long cozinhaId) {

        Cozinha cozinhaAtual = cadastroCozinhaService.hasOrNot(cozinhaId);
        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
        return  cadastroCozinhaService.salvar(cozinhaAtual);

    }



@DeleteMapping(value = "/{cozinhaId}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void remover(@PathVariable Long cozinhaId) {

    cadastroCozinhaService.excluir(cozinhaId);

}


}


