package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Restaurante;
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
    public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {

        Optional<Cidade> cidade = cadastrarCidadeService.buscar(cidadeId);

        if (cidade.isPresent()) {
            return ResponseEntity.ok(cidade.get());
        }

        return ResponseEntity.notFound().build();
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
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId,
                                       @RequestBody Cidade cidade) {
        try {

            Optional<Cidade> cidadeAtual = cadastrarCidadeService.buscar(cidadeId);

            if (cidadeAtual.isPresent()) {
                BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
               Cidade cidadeSalva = cadastrarCidadeService.salvar(cidadeAtual.get());
                return ResponseEntity.ok(cidadeSalva);
            }
            return ResponseEntity.notFound().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    @DeleteMapping(value = "/{cidadeId}")
    public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId
        try {

            cadastrarCidadeService.excluir(cidadeId);
            return ResponseEntity.noContent().build();

        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());//A entidade não foi encontrada...
        } catch (EntidadeEmUsoException e){
            // Se a chave  estrangeira recurso numa outra tabela então essa exceção será capturada aqui como "conflito"
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
