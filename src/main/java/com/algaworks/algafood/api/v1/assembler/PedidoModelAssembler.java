package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FactoryLinks factoryLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    public PedidoModel toModel(Pedido pedido) {
        var pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        // Não usei o método algaSecurity.podePesquisarPedidos(clienteId, restauranteId) aqui,
        // porque na geração do link, não temos o id do cliente e do restaurante,
        // então precisamos saber apenas se a requisição está autenticada e tem o escopo de leitura
        if (algaSecurity.podePesquisarPedidos()) {
            pedidoModel.add(factoryLinks.linkToPedidos("pedidos"));

        }

        if (algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) { //Renderizams os links abaixo de acordo com as permissões do user.
            //Adicionando links de forma condicional..
            if (pedido.podeSerConfirmado()) {
                pedidoModel.add(factoryLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
            }

            //Adicionando links de forma condicional..
            if (pedido.podeSerCancelado()) {
                pedidoModel.add(factoryLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
            }

            //Adicionando links de forma condicional..
            if (pedido.podeSerEntregue()) {
                pedidoModel.add(factoryLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
            }
        }


        if (algaSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getRestaurante().add(
                    factoryLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }


        if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(
                    factoryLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        if (algaSecurity.podeConsultarFormasPagamento()) {
            pedidoModel.getFormaPagamento().add(
                    factoryLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));

        }



        if (algaSecurity.podeConsultarCidades()) {
            pedidoModel.getEndereco().getCidade().add(
                    factoryLinks.linkToCidade(pedido.getEndereco().getCidade().getId()));

        }


        // Quem pode consultar restaurantes, também pode consultar os produtos dos restaurantes
        if (algaSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getItens().forEach(item -> {
                item.add(factoryLinks.linkToProduto(
                        pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
            });
        }



        return pedidoModel;
    }

    @Override
    public CollectionModel<PedidoModel> toCollectionModel(Iterable<? extends Pedido> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(PedidoController.class).withSelfRel());
    }


}