package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.RestauranteUsuario;
import com.algaworks.algafood.domain.model.RestauranteUsuarioId;
import com.algaworks.algafood.domain.repository.RestauranteUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarRestauranteUsuarioService {

    @Autowired
    RestauranteUsuarioRepository restauranteUsuarioRepository;

    public RestauranteUsuario findById(Long restauranteId, Long usuarioId){

        RestauranteUsuarioId restauranteUsuarioId = RestauranteUsuarioId.builder().
                restauranteId(restauranteId).
                usuarioId(usuarioId).build();

        return restauranteUsuarioRepository.findById(restauranteUsuarioId).orElseThrow(
                () -> new UsuarioNaoEncontradoException(
                        usuarioId,restauranteId)
        );
    }
}
