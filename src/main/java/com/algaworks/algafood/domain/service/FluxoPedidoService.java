package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;
    private static final String TEMPLATE_PEDIDO_CONFIRMADO = "pedido-confirmado.html";


    @Autowired
    private EnvioEmailService envioEmail;

    @Autowired
    private PedidoRepository pedidoRepository;

    //O pedido precisa estar com o status criada...
    @Transactional
    public void confirmar(String codigoPedido) {

        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.confirmar();
        pedidoRepository.save(pedido); // É necessário para registrar o event

    }

    @Transactional
    public void cancelamento(String codigoPedido) {
        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.cancelar();
        pedidoRepository.save(pedido); // É necessário para registrar o event
    }

    @Transactional
    public void entrega(String codigoPedido) {

        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.entregar();

    }
}
