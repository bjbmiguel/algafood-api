package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.GrupoPermissao;
import com.algaworks.algafood.domain.model.GrupoPermissaoId;
import com.algaworks.algafood.domain.model.UsuarioGrupo;
import com.algaworks.algafood.domain.model.UsuarioGrupoId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioGrupoRepository extends CustomJpaRepository<UsuarioGrupo, UsuarioGrupoId>{

    Optional<UsuarioGrupo> findById(UsuarioGrupoId usuarioGrupoId);

}
