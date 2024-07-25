package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ValorZeroIncluiDescricao(valorField = "taxaFrete",
        descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
public class Restaurante {

    @Id
    @EqualsAndHashCode.Include // Vair criar os métodos equas e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private Long id;

    @NotBlank//(groups = Groups.CadastroRestaurante.class) // Esta constraint está agrupa num grupo CadastroRestaurante
    private String nome;

    @NotNull
    @PositiveOrZero//(groups = Groups.CadastroRestaurante.class)
    @Column(name = "taxa_frete", nullable = false)  //nullable --> not null
    private BigDecimal taxaFrete;

    @Column
    private Boolean ativo = Boolean.TRUE;

    @Column
    private Boolean aberto = Boolean.TRUE;

    @ManyToOne //(fetch = FetchType.LAZY) // ... vários restaurantes podem estar associado a uma cozinha,
    @JoinColumn(name = "cozinha_id", nullable = false) // Usamos para especificar o nome da chave estrangeira... fk
    private Cozinha cozinha;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    Set<FormaPagamento> formasPagamento = new HashSet<>(); // Contém as formas de pagamento de um rest específico...

    @Embedded // é uma imcorporação..., i.e, esta classe é uma parte da classe Restaurante.
    private Endereco endereco;

    @CreationTimestamp
    // Essa anotação é do hibernente (implementação ) e  não do JPA..., uma data-hora será inserida de form aut.
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;

    @UpdateTimestamp //Vai atribiot uma hora e data de forma automat. sempre a entidade for actualizada
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;

    @OneToMany(mappedBy = "restaurante")
    List<Produto> produotos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "restaurante_usuario_responsavel",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    Set<Usuario> usuarios = new HashSet<>(); // Contém os usuários responsáveis por um rest específico...

    public void ativar() {
        this.setAtivo(true);
    }

    public void inativar() {
        this.setAtivo(false);
    }

    public void abrir() {
        this.setAberto(true);
    }

    public void fechar() {
        this.setAberto(false);
    }

    public boolean podeAtivar() {
        return !getAtivo();
    }

    public boolean podeInativar() {
        return !podeAtivar();
    }

    public boolean podeAbrir() {
        return !getAberto();
    }

    public boolean podeFechar() {
        return !podeAbrir();
    }


    public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().remove(formaPagamento);
    }

    public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().add(formaPagamento);
    }

    public boolean adicionarUsuarioResponsavel(Usuario usuario) {
        return getUsuarios().add(usuario);
    }

    public boolean removerUsuarioResponsavel(Usuario usuario) {
        return getUsuarios().remove(usuario);
    }

    public Optional<FormaPagamento> validateFormaPagamentoById(Long formaPagamentoId) {

        for (FormaPagamento formaPagamento : this.formasPagamento) {
            if (formaPagamento.getId().equals(formaPagamentoId)) return Optional.of(formaPagamento);
        }
        return Optional.empty();
    }


    public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().contains(formaPagamento);
    }

    public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
        return !aceitaFormaPagamento(formaPagamento);
    }


}
