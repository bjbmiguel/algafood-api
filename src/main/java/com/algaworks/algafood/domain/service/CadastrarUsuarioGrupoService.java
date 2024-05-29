package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.UsuarioGrupo;
import com.algaworks.algafood.domain.model.UsuarioGrupoId;
import com.algaworks.algafood.domain.repository.UsuarioGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarUsuarioGrupoService {


    @Autowired
    UsuarioGrupoRepository usuarioGrupoRepository;


    public UsuarioGrupo findById(Long usuarioId, Long grupoId) {

        UsuarioGrupoId usuarioGrupoId = UsuarioGrupoId.builder().
                grupoId(grupoId).
                usuarioId(usuarioId).
                build();
        return usuarioGrupoRepository.findById(usuarioGrupoId).orElseThrow(
                () -> new GrupoNaoEncontradoException(
                        grupoId,usuarioId));
    }
}
