package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/cozinhas") // Esta anotação é usada para mapear as req HTTP feitas neste controller, ou seja, todas as reqs. /cozinhas vão cair aqui...
@RestController // Esta anotação define a classe num controlador de modos que ela possa lidar com requisições HTTP e devolver respostas...
public class CozinhaController {

    //TODO analisar o comportamento do usdo da anotação  @GetMapping sem argumento em dois métodos
    //TODO anaisar o comportamento do uso da anotação @RequestMapping directamente nos métodos

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Requisições com HTTP do tipo Get vão cair aqui...
    public List<Cozinha> listar(){
        System.out.printf("LIST 1\n");
        return cozinhaRepository.todas();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE) // Requisições com HTTP do tipo Get vão cair aqui...
    public List<Cozinha> listar2(){
        System.out.printf("LIST 2\n");
        return cozinhaRepository.todas();
    }


}


