package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "grupo_permissao")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GrupoPermissao {

    @EmbeddedId
    private GrupoPermissaoId id;

    @MapsId("grupoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_grupo_permissao_grupo"))
    private Grupo grupo;


    @MapsId("permissaoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "permissao_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_grupo_permissao_permissao"))
    private Permissao permissao;

}
