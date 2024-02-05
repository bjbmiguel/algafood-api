package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<Cozinha> listar() {

        return cozinhaRepository.todas();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXML() {

        return new CozinhasXmlWrapper(cozinhaRepository.todas());
    }

    @GetMapping(value = "/{cozinhaId}")
    // Usmos a classe ResponseEntity para costumizar respostas HTTP...
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {  // Será feito um bind de forma automática

        HttpHeaders httpHeaders = new HttpHeaders(); //Usamos para adicionar cabeçalhos da resposta
        httpHeaders.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas"); //Adicionamos um location

        // O status Found - 302 indica que o recurso foi movido para outra URI...
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();

    }

}


