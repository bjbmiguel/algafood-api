package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Pedido;
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

    //O pedido precisa estar com o status criada...
    @Transactional
    public void confirmar(String codigoPedido) {

        Pedido pedido = emissaoPedidoService.findByCodigo(codigoPedido);
        pedido.confirmar();

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
                .corpo(TEMPLATE_PEDIDO_CONFIRMADO)
                .destinatario(pedido.getCliente().getEmail())
                .variavel("pedido", pedido)
                .build();

        envioEmail.enviar(mensagem);
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
