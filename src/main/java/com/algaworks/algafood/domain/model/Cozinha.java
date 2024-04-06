package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity // A classe representa uma entidade
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cozinha {

    @Id
    @NotNull(groups = Groups.CozinhaId.class)
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    //@JsonProperty("titulo")
    //@JsonIgnore // Vai ignorar este campo na  representação do recurso.
    @NotBlank // Padrão está no grupo default
    @Column(nullable = true)
    private String nome;

    @OneToMany(mappedBy = "cozinha")
    List<Restaurante> restaurantes  = new ArrayList<>();


}
