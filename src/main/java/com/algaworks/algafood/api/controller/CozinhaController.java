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
    // Usmos a classe ResponseEntity para costumizar respostas HTTP...
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        //Este método nunca vai retornar um valor "null" sempre irá retornar um optional que pode ou ter uma cozinha...
        Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);


        if (cozinha.isPresent()) {

            return ResponseEntity.ok().body(cozinha.get()); // O método body representa o corpo da resposta.
        }

        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build(); //Optimizando a resposta...

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
    public ResponseEntity<Cozinha> actualizar(@RequestBody Cozinha cozinha, @PathVariable Long cozinhaId) {
        // @RequestBody vai fazer o bind de forma automática para o objecto cozinha
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId

        //Pegando a cozinha existente...
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);

        if (cozinhaAtual.isPresent()) {
            //BeanUtils esta classe é do Spring...
            // O id será ignorado...
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
            Cozinha cozinhaSalva = cadastroCozinhaService.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(cozinhaSalva); //O método body representa o corpo da resposta.
        }
        return ResponseEntity.notFound().build(); //Optimizando a resposta...
    }

    /*
    @DeleteMapping(value = "/{cozinhaId}")

    public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId
        try {

            cadastroCozinhaService.excluir(cozinhaId);
            return ResponseEntity.noContent().build();

        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //A entidade não foi encontrada...
        } catch (EntidadeEmUsoException e){
            // Se a chave  estrangeira recurso numa outra tabela então essa exceção será capturada aqui como "conflito"
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
*/
    @DeleteMapping(value = "/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId) {

            cadastroCozinhaService.excluir(cozinhaId);

    }


}


