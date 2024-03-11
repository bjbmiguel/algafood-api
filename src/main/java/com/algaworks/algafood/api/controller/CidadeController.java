package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CadastrarCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {

        try {

            cidade = cadastrarCidadeService.salvar(cidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);

        } catch (EntidadeNaoEncontradaException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable Long cidadeId,
                            @RequestBody Cidade cidade) {

            Cidade cidadeAtual = cadastrarCidadeService.hasOrNot(cidadeId);
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return cadastrarCidadeService.salvar(cidadeAtual);

        }

        @DeleteMapping(value = "/{cidadeId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void remover (@PathVariable Long cidadeId){

            cadastrarCidadeService.excluir(cidadeId);

        }

    }
