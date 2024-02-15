package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastrarEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/estados")
@RestController // Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class EstadoController {

    @Autowired // Injetamos a dependência
    private CadastrarEstadoService cadastrarEstadoService;

    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<Estado> listar() {
        return cadastrarEstadoService.listar();
    }

    @GetMapping(value = "/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {

        Estado estado = cadastrarEstadoService.buscar(estadoId);

       if (estado != null) {

            return ResponseEntity.ok().body(estado); // O método body representa o corpo da resposta.
        }

       return ResponseEntity.notFound().build(); //Optimizando a resposta... 404

    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public Estado adicionar(@RequestBody Estado estado) { //Anotamos o parâmetro "estado"
        return cadastrarEstadoService.salvar(estado);
    }


    @PutMapping(value = "/{estadoId}")
    public ResponseEntity<Estado> actualizar(@RequestBody Estado estado, @PathVariable Long estadoId) {

        // @RequestBody vai fazer o bind de forma automática para o objecto cozinha
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId

        //Pegando a cozinha existente...
        Estado estadoAtual = cadastrarEstadoService.buscar(estadoId);

        if (estadoAtual != null) {
            //BeanUtils esta classe é do Spring...
            // O id será ignorado...
            BeanUtils.copyProperties(estado, estadoAtual, "id");
            cadastrarEstadoService.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual); //O método body representa o corpo da resposta.
        }

        return ResponseEntity.notFound().build(); //Optimizando a resposta...
    }

    @DeleteMapping(value = "/{estadoId}")
    public ResponseEntity<Estado> remover(@PathVariable Long estadoId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId
        try {

            cadastrarEstadoService.excluir(estadoId);
            return ResponseEntity.noContent().build();

        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build(); //A entidade não foi encontrada...
        } catch (EntidadeEmUsoException e){
            // Se a chave  estrangeira recurso numa outra tabela então essa exceção será capturada aqui como "conflito"
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
