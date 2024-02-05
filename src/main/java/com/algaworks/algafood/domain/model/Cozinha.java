package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity // A classe representa uma entidade
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cozinha {

    @Id
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    //@JsonProperty("titulo")
    //@JsonIgnore // Vai ignorar este campo na  representação do recurso.
    @Column(nullable = true)
    private String nome;


}
