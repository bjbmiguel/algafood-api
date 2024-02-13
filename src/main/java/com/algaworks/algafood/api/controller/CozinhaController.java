package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return cozinhaRepository.todas();
    }


    @GetMapping(value = "/{cozinhaId}")
    // Usmos a classe ResponseEntity para costumizar respostas HTTP...
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        Cozinha cozinha = cozinhaRepository.porId(cozinhaId);

        if (cozinha != null) {

            return ResponseEntity.ok().body(cozinha); // O método body representa o corpo da resposta.
        }

        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build(); //Optimizando a resposta...

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
        Cozinha cozinhaAtual = cozinhaRepository.porId(cozinhaId);

        if (cozinhaAtual != null) {
            //BeanUtils esta classe é do Spring...
            // O id será ignorado...
            BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
            cadastroCozinhaService.salvar(cozinhaAtual);
            return ResponseEntity.ok(cozinhaAtual); //O método body representa o corpo da resposta.
        }
        return ResponseEntity.notFound().build(); //Optimizando a resposta...
    }


    @DeleteMapping(value = "/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId
        try {

            cadastroCozinhaService.excluir(cozinhaId);
            return ResponseEntity.noContent().build();

        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build(); //A entidade não foi encontrada...
        } catch (EntidadeEmUsoException e){
            // Se a chave  estrangeira recurso numa outra tabela então essa exceção será capturada aqui como "conflito"
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}


