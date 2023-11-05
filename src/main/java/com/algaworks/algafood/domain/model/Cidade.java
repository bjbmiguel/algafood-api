package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity // A classe representa uma entidade
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cidade {

    @Id
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;
    @Column(nullable = true)
    private String nome;

    @ManyToOne // ... várias cidades podem estar associadas a um estado,
    @JoinColumn(name = "estado_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Estado estado;
}
