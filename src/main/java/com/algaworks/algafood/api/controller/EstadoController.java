package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/estados")
@RestController // Definimos a classe Estado como um controlador para lidar com req HTTP RESTFul que retornam resp em form XML e JSON
public class EstadoController {

    @Autowired // Injetamos a dependência
    private EstadoRepository estadoRepository;

    @GetMapping // Mapeamos as requ HTTP do tipo GET para este método
    public List<Estado> listar(){
        return estadoRepository.todas();
    }
}
