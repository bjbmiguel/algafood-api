package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.GrupoPermissao;
import com.algaworks.algafood.domain.model.GrupoPermissaoId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupoPermissaoRepository extends CustomJpaRepository<GrupoPermissao, GrupoPermissaoId>{

    Optional<GrupoPermissao> findById(GrupoPermissaoId grupoPermissaoId);

}
