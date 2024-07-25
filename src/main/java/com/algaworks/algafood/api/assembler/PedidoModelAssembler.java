package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.FactoryLinks;
import com.algaworks.algafood.api.controller.*;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FactoryLinks factoryLinks;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    public PedidoModel toModel(Pedido pedido) {
        var pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(factoryLinks.linkToPedidos());

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
        pedidoModel.getRestaurante().add(
                factoryLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(
                factoryLinks.linkToUsuario(pedido.getCliente().getId()));

        pedidoModel.getFormaPagamento().add(
                factoryLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));

        pedidoModel.getEndereco().getCidade().add(
                factoryLinks.linkToCidade(pedido.getEndereco().getCidade().getId()));

        pedidoModel.getItens().forEach(item -> {
            item.add(factoryLinks.linkToProduto(
                    pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
        });

        return pedidoModel;
    }

    @Override
    public CollectionModel<PedidoModel> toCollectionModel(Iterable<? extends Pedido> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(PedidoController.class).withSelfRel());
    }


}