package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.UsuarioController;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.core.modelmapper.ModelMapperConfig;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

    @Autowired
    private ModelMapperConfig mapperConfig;

    @Autowired
    FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    public UsuarioModelAssembler() {
        super(UsuarioController.class, UsuarioModel.class);
    }

    @Override
    public UsuarioModel toModel(Usuario usuario) {
        var usuarioModel= createModelWithId(usuario.getId(), usuario);
        mapperConfig.modelMapper().map(usuario, usuarioModel);

        if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {

            usuarioModel.add(links.linkToUsuarios("usuarios"));
            usuarioModel.add(links.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));

        }

        return usuarioModel;
    }

    @Override
    public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities)
                .add(links.linkToUsuarios());
    }

}