package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;
import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ValorZeroIncluiDescricao(valorField = "taxaFrete",
        descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
public class Restaurante {

    @Id
    @EqualsAndHashCode.Include // Vair criar os métodos equas e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    @NotBlank//(groups = Groups.CadastroRestaurante.class) // Esta constraint está agrupa num grupo CadastroRestaurante
    private String nome;

    @NotNull
    @PositiveOrZero//(groups = Groups.CadastroRestaurante.class)
    @Column(name = "taxa_frete", nullable = false)  //nullable --> not null
    private BigDecimal taxaFrete;


    private Boolean ativo = Boolean.TRUE;


    @NotNull//(groups = Groups.CadastroRestaurante.class)
    @ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
    @Valid // será feito uma validação em cascata, ou seja, vai validar as proprieddades do objecto Cozinha
    @ManyToOne //(fetch = FetchType.LAZY) // ... vários restaurantes podem estar associado a uma cozinha,
    @JoinColumn(name = "cozinha_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Cozinha cozinha;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
    joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    List<FormaDePagamento> formasPagamento = new ArrayList<>();

    @Embedded // é uma imcorporação..., i.e, esta classe é uma parte da classe Restaurante.
    private Endereco endereco;


    @CreationTimestamp // Essa anotação é do hibernente (implementação ) e  não do JPA..., uma data-hora será inserida de form aut.
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;


    @UpdateTimestamp //Vai atribiot uma hora e data de forma automat. sempre a entidade for actualizada
    @Column(nullable = false , columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;


    @OneToMany(mappedBy = "restaurante")
    List<Produto> produotos = new ArrayList<>();

    public void ativar(){

        this.setAtivo(true);
    }

    public void inativar(){

        this.setAtivo(false);
    }

}
