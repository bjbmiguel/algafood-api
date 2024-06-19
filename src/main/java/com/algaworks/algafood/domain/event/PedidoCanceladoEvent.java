package com.algaworks.algafood.domain.event;

import com.algaworks.algafood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {// Convém manter o nome da classe no passado para indicar uma ação que já aconteceu

    private Pedido pedido;

}