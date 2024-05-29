package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastrarRestauranteUsuarioService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
@RestController
public class RestauranteUsuarioController {

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    CadastrarRestauranteUsuarioService cadastrarRestauranteUsuarioService;

    @GetMapping
    public List<UsuarioModel> listar(@PathVariable Long restauranteId){

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);

        return  usuarioModelAssembler.toCollectionModel(restaurante.getUsuarios());
    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarResponsavel(@PathVariable  Long restauranteId,
                                     @PathVariable  Long usuarioId){

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);
        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);

        cadastratarRestauranteService.adicionarResponsavel(restaurante, usuario);

    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerResponsavel(@PathVariable Long restauranteId, @PathVariable
    Long usuarioId){

        cadastratarRestauranteService.checkIfExistsById(restauranteId);
        RestauranteUsuario restauranteUsuario = cadastrarRestauranteUsuarioService.findById(restauranteId, usuarioId);

        cadastratarRestauranteService.removerResponsavel(restauranteUsuario.getRestaurante(), restauranteUsuario.getUsuario());
    }





}
