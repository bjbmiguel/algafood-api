package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    //@JsonIgnore  //Igonorando a serialização de Cozinha na repr. do recurso.
    //@JsonIgnoreProperties("hibernateLazyInitializer") //ignorando a propriedade hibernateLazyInitializer de Cozinha
    @ManyToOne //(fetch = FetchType.LAZY) // ... vários restaurantes podem estar associado a uma cozinha,
    @JoinColumn(name = "cozinha_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Cozinha cozinha;

    //@JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
    joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    List<FormaDePagamento> formasPagamento = new ArrayList<>();

    @JsonIgnore
    @Embedded // é uma imcorporação..., i.e, esta classe é uma parte da classe Restaurante.
    private Endereco endereco;

    @JsonIgnore
    @CreationTimestamp // Essa anotação é do hibernente (implementação ) e  não do JPA..., uma data-hora será inserida de form aut.
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataCadastro;

    @JsonIgnore
    @UpdateTimestamp //Vai atribiot uma hora e data de forma automat. sempre a entidade for actualizada
    @Column(nullable = false , columnDefinition = "datetime")
    private LocalDateTime dataAtualizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurante")
    List<Produto> produotos = new ArrayList<>();


}
