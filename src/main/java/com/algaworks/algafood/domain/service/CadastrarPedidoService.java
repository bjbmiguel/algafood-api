package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastrarPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public  Pedido findById(Long pedidoId){

        return pedidoRepository.findById(pedidoId).orElseThrow(
                () -> new PedidoNaoEncontradoException(
                        pedidoId));
    }

    @Transactional
    public Pedido salvar(Pedido pedido){

        pedido.calcularPrecoTotal();
        pedido.calcularSubtotal();
        pedido.calcularValorTotal();
        return  pedidoRepository.save(pedido);

    }
}
