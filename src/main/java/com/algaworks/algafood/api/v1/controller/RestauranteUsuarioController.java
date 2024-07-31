package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastrarRestauranteUsuarioService;
import com.algaworks.algafood.domain.service.CadastrarUsuarioService;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/v1/restaurantes/{restauranteId}/responsaveis")
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

        CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
                .toCollectionModel(restaurante.getUsuarios())
                .removeLinks()
                .add(links.linkToRestauranteResponsaveis(restauranteId))
                .add(links.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

        usuariosModel.getContent().stream().forEach(usuarioModel -> {
            usuarioModel.add(links.linkToRestauranteResponsavelDesassociacao(
                    restauranteId, usuarioModel.getId(), "desassociar"));
        });

        return usuariosModel;
    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarResponsavel(@PathVariable Long restauranteId,
                                                     @PathVariable Long usuarioId) {

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);
        Usuario usuario = cadastrarUsuarioService.findById(usuarioId);

        cadastratarRestauranteService.adicionarResponsavel(restaurante, usuario);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  removerResponsavel(@PathVariable Long restauranteId, @PathVariable
    Long usuarioId) {

        cadastratarRestauranteService.checkIfExistsById(restauranteId);
        RestauranteUsuario restauranteUsuario = cadastrarRestauranteUsuarioService.findById(restauranteId, usuarioId);

        cadastratarRestauranteService.removerResponsavel(restauranteUsuario.getRestaurante(), restauranteUsuario.getUsuario());

        return ResponseEntity.noContent().build();
    }


}
