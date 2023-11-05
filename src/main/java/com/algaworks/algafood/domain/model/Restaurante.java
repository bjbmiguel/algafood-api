package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Restaurante {

    @Id
    @EqualsAndHashCode.Include // Vair criar os métodos equas e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    private String nome;

    @Column(name = "taxa_frete", nullable = false)  //nullable --> not null
    private BigDecimal taxaFrete;

    @ManyToOne // ... vários restaurantes podem estar associado a uma cozinha,
    @JoinColumn(name = "cozinha_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Cozinha cozinha;

}
