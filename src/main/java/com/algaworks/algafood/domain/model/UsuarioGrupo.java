package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "usuario_grupo")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioGrupo {

    @EmbeddedId
    private UsuarioGrupoId id;

    @MapsId("usuarioId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_grupo_usuario"))
    private Usuario usuario;


    @MapsId("grupoId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_grupo_grupo"))
    private Grupo grupo;



}
