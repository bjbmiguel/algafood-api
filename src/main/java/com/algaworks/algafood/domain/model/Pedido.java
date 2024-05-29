package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @EqualsAndHashCode.Include // Vai criar os métodos equals e hascod usando apenas o ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usando o "GenerationType.IDENTITY" quem vai a gerar a PK é o mysql...
    private  Long id;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxaFrete;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Embedded
    private Endereco endereco;

    @Column(length = 10, nullable = false)

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCriacao;

    @Column(columnDefinition = "datetime")
    private OffsetDateTime dataConfirmacao;

    @Column(columnDefinition = "datetime")
    private OffsetDateTime dataEntrega;

    @Column(columnDefinition = "datetime")
    private OffsetDateTime dataCancelamento;

    @ManyToOne(fetch = FetchType.LAZY) //So faz o select quando um get na instância "formaPagamento" for feito
    @JoinColumn(name = "forma_pagamento_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_forma_pagamento"))
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_restaurante"))
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_usuario_cliente"))
    private Usuario cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) //CascadeTypeAll = P/cadastrar os itens do pedido...
    private Set<ItemPedido> itens = new HashSet<>();

    public void calcularPrecoTotal(){
        this.getItens().forEach(ItemPedido::calcularPrecoTotal);
    }

    public void calcularSubtotal(){
        this.setSubtotal(getItens().stream().map(ItemPedido::getPrecoTotal).reduce(BigDecimal.valueOf(0), BigDecimal::add));
    }

    public void calcularValorTotal(){

        BigDecimal subtotal = getSubtotal();
        this.setTaxaFrete(this.getRestaurante().getTaxaFrete());
        this.setValorTotal(subtotal.add(getTaxaFrete()));
    }

}
