package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class FluxoPedidoService {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    //O pedido precisa estar com o status criada...
    @Transactional
    public void confirmar(Long pedidoId){

        Pedido pedido = emissaoPedidoService.findById(pedidoId);

        if (!pedido.isCreated()){
            throw  new NegocioException(String.format("Status do pedido %d não pode ser alterado de %s para %s",
                    pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.CRIADO.getDescricao()));
        }
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataConfirmacao(OffsetDateTime.now());

    }

    @Transactional
    public void cancelamento(Long pedidoId){

        Pedido pedido = emissaoPedidoService.findById(pedidoId);

        if (!pedido.isCreated()){
            throw  new NegocioException(String.format("Status do pedido %d não pode ser alterado de %s para %s",
                    pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.CANCELADO.getDescricao()));
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataConfirmacao(OffsetDateTime.now());
    }

    @Transactional
    public void entrega(Long pedidoId){

        Pedido pedido = emissaoPedidoService.findById(pedidoId);

        if (!pedido.isConfirmed()){
            throw  new NegocioException(String.format("Status do pedido %d não pode ser alterado de %s para %s",
                    pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.ENTREGUE.getDescricao()));
        }
        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDataConfirmacao(OffsetDateTime.now());

    }
}
