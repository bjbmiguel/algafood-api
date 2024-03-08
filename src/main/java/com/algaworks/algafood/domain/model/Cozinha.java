package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.loader.collection.OneToManyJoinWalker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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


    @JsonIgnore // vai ignorar a serialização de restaurante quando for serializar a cozinha.
    @OneToMany(mappedBy = "cozinha")
    List<Restaurante> restaurantes  = new ArrayList<>();


}
