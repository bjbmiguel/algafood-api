package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.GrupoPermissao;
import com.algaworks.algafood.domain.model.GrupoPermissaoId;
import com.algaworks.algafood.domain.repository.GrupoPermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarGrupoPermissaoService {


    @Autowired
    GrupoPermissaoRepository grupoPermissaoRepository;


    public GrupoPermissao findById(Long grupoId, Long permissaoId) {

        GrupoPermissaoId grupoPermissaoId = GrupoPermissaoId.builder().
                grupoId(grupoId).
                permissaoId(permissaoId).
                build();
        return grupoPermissaoRepository.findById(grupoPermissaoId).orElseThrow(
                () -> new PermissaoNaoEncontradaException(
                        grupoId,permissaoId));
    }
}
