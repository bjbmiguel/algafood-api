package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

@Entity // A classe representa uma entidade
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cidade {

    @Id
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    @NotBlank
    @Column(nullable = true)
    private String nome;

    @NotNull
    @ConvertGroup(from = Default.class, to = Groups.EstadoId.class) // Vai validar somente as proprie, do grupo EstadoId
    @Valid // vai validar o objecto estado de acordo com as validações definidas na classe...
    @ManyToOne // ... várias cidades podem estar associadas a um estado,
    @JoinColumn(name = "estado_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Estado estado;
}
