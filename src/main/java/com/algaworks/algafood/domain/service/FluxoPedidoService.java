package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    //O pedido precisa estar com o status criada...
    @Transactional
    public void confirmar(String codigoPedido) {

        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.confirmar();
    }

    @Transactional
    public void cancelamento(String codigoPedido) {
        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.cancelar();
    }

    @Transactional
    public void entrega(String codigoPedido) {

        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.entregar();

    }
}
