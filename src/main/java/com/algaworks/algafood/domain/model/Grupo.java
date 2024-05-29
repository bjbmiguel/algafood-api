package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // A classe representa uma entidade
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Grupo {

    @Id
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    @Column(nullable = false)
    private String nome;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "grupo_permissao",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    Set<Permissao> permissoes = new HashSet<>(); // Uma collection do tipo set n permite itens duplicado...

    public void removerPermissao(Permissao permissao){
        this.getPermissoes().remove(permissao);
    }

    public void adicionarPermissao(Permissao permissao){
        this.getPermissoes().add(permissao);
    }
}
