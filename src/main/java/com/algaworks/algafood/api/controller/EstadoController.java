package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastrarEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/estados")
@RestController
// Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class EstadoController {

    @Autowired // Injetamos a dependência
    private CadastrarEstadoService cadastrarEstadoService;

    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<Estado> listar() {
        return cadastrarEstadoService.listar();
    }

    @GetMapping(value = "/{estadoId}")
    public Estado buscar(@PathVariable Long estadoId) {

        return cadastrarEstadoService.hasOrNot(estadoId);

    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public Estado adicionar(@RequestBody Estado estado) { //Anotamos o parâmetro "estado"
        return cadastrarEstadoService.salvar(estado);
    }


    @PutMapping(value = "/{estadoId}")
    public Estado actualizar(@RequestBody Estado estado, @PathVariable Long estadoId) {

        //Busca estado... ou lança uma exceção que esta mapeado com NOT_FOUND
        Estado estadoAtual = cadastrarEstadoService.hasOrNot(estadoId);

        //Copia as propriedade de estado --> eatddoAtual com exceção ao id
        BeanUtils.copyProperties(estado, estadoAtual, "id");

        //Salvar, e o seu retorno vai para o corpo da resposta da requisição
        return cadastrarEstadoService.salvar(estadoAtual);
    }

    @DeleteMapping(value = "/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long estadoId) {

        cadastrarEstadoService.excluir(estadoId);

    }

}
