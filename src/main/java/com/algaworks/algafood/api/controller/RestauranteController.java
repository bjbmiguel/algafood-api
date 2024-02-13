package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/restaurantes")
@RestController
public class RestauranteController {


    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {

        return cadastratarRestauranteService.todos();
    }

    @GetMapping(value = "/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {

        try {

            Restaurante restaurante = cadastratarRestauranteService.buscar(restauranteId);
            return ResponseEntity.ok().body(restaurante);

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Restaurante> adicionar(){
        
    }


}
