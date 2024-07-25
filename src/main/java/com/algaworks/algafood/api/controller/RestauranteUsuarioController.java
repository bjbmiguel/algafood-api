package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastrarRestauranteUsuarioService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import com.lowagie.text.html.simpleparser.ALink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
@RestController
public class RestauranteUsuarioController implements RestauranteUsuarioResponsavelControllerOpenApi {

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    CadastrarRestauranteUsuarioService cadastrarRestauranteUsuarioService;

    @Autowired
    FactoryLinks links;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {

        var restaurante = cadastratarRestauranteService.findById(restauranteId);

        var usuarioCollectionModel = usuarioModelAssembler.toCollectionModel(restaurante.getUsuarios());

        usuarioCollectionModel.removeLinks().add(links.linkToResponsaveisRestaurante(restauranteId));

        return usuarioCollectionModel;
    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarResponsavel(@PathVariable Long restauranteId,
                                     @PathVariable Long usuarioId) {

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);
        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);

        cadastratarRestauranteService.adicionarResponsavel(restaurante, usuario);

    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerResponsavel(@PathVariable Long restauranteId, @PathVariable
    Long usuarioId) {

        cadastratarRestauranteService.checkIfExistsById(restauranteId);
        RestauranteUsuario restauranteUsuario = cadastrarRestauranteUsuarioService.findById(restauranteId, usuarioId);

        cadastratarRestauranteService.removerResponsavel(restauranteUsuario.getRestaurante(), restauranteUsuario.getUsuario());
    }


}
