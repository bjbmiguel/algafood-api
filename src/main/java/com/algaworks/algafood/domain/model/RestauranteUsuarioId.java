package com.algaworks.algafood.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@Builder
//@NoArgsConstructor  // Para evitar No default constructor for entity:  : com.algaworks.algafood.domain.model.RestauranteUsuarioId
@AllArgsConstructor
public class RestauranteUsuarioId implements Serializable {

    @EqualsAndHashCode.Include
    @Column(name = "restaurante_id")
    private Long restauranteId;

    @EqualsAndHashCode.Include
    @Column(name = "usuario_id")
    private Long usuarioId;

}
