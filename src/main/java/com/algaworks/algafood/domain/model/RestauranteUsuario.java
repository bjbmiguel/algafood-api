package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "restaurante_usuario_responsavel")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RestauranteUsuario {

    @EmbeddedId
    RestauranteUsuarioId id;

    @MapsId("restauranteId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_restaurante_usuario_restaurante"))
    private Restaurante restaurante;

    @MapsId("usuarioId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_restaurante_usuario_usuario"))
    private Usuario usuario;





}
