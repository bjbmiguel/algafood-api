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
public class RestauranteUsuarioId implements Serializable {

    @EqualsAndHashCode.Include
    @Column(name = "restaurante_id")
    private Long restauranteId;

    @EqualsAndHashCode.Include
    @Column(name = "usuario_id")
    private Long usuarioId;



}
