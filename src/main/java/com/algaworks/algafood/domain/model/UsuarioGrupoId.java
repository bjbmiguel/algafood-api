package com.algaworks.algafood.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@Builder
public class UsuarioGrupoId implements Serializable {



    @EqualsAndHashCode.Include
    @Column(name = "usuario_id")
    private Long usuarioId;

    @EqualsAndHashCode.Include
    @Column(name = "grupo_id")
    private Long grupoId;

}
