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

        //Adicionamos link na representação do recurso pedido em restaurante
        pedidoModel.getRestaurante().add(linkTo(methodOn(RestauranteController.class)
                .buscar(pedido.getRestaurante().getId())).withSelfRel());

        pedidoModel.getCliente().add(linkTo(methodOn(UsuarioController.class)
                .buscar(pedido.getCliente().getId())).withSelfRel());

        pedidoModel.getFormaPagamento().add(linkTo(methodOn(FormaPagamentoController.class)
                .buscar(pedido.getFormaPagamento().getId(), null)).withSelfRel());


        pedidoModel.getEndereco().getCidade().add(linkTo(methodOn(CidadeController.class)
                .buscar(pedido.getEndereco().getCidade().getId())).withSelfRel());

        //Adicionando link em coleção...
        pedidoModel.getItens().forEach(item -> {
            //ItemPedido precisa ser um representation model...
            item.add(linkTo(methodOn(ProdutoRestauranteController.class)
                    .buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
                    .withRel("produto"));
        });


        pedidoModel.add(factoryLinks.linkToPedidos());

        return pedidoModel;
    }

    @Override
    public CollectionModel<PedidoModel> toCollectionModel(Iterable<? extends Pedido> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(PedidoController.class).withSelfRel());
    }


}