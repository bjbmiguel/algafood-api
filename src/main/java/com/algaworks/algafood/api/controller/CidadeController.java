package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.api.exceptionhanlder.Problema;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CadastrarCidadeService;
import lombok.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RequestMapping("/cidades")
@RestController
// Definimos a classe como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class CidadeController {

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;

    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<Cidade> listar() {
        return cadastrarCidadeService.listar();
    }

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable Long cidadeId) {

        return cadastrarCidadeService.hasOrNot(cidadeId);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    // Usamos o ? para aceitar qualquer tipo de parâmetro...
    public Cidade adicionar(@RequestBody Cidade cidade) {

        try {
            return cadastrarCidadeService.salvar(cidade);

        } catch (EstadoNaoEncontradaException e) {

            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable Long cidadeId,
                            @RequestBody Cidade cidade) {

        //Qualquer exceção EntidadeNaoEncontradaException ou de suas subclasses serão tratadas pelo método anotado com
        // @ExceptionHandler(EntidadeNaoEncontradaException.class)
        Cidade cidadeAtual = cadastrarCidadeService.hasOrNot(cidadeId);
        BeanUtils.copyProperties(cidade, cidadeAtual, "id");

        try {

            return cadastrarCidadeService.salvar(cidadeAtual);

        } catch (EstadoNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);//Passando a causa (método que lançou) também...
        }

    }

    @DeleteMapping(value = "/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {

        cadastrarCidadeService.excluir(cidadeId);

    }


}
