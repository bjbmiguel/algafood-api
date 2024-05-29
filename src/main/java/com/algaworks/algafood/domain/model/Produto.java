package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {
    @Id
    @EqualsAndHashCode.Include // Vair criar os métodos equas e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    Restaurante restaurante;
}
