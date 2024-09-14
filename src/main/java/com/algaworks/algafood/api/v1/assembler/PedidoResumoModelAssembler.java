package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FactoryLinks factoryLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    public PedidoResumoModel toModel(Pedido pedido) {

        PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        if (algaSecurity.podePesquisarPedidos()) {
            pedidoModel.add(factoryLinks.linkToPedidos("pedidos"));

        }

        if (algaSecurity.podeConsultarRestaurantes()) {
            pedidoModel.getRestaurante().add(factoryLinks.linkToRestaurante(pedido.getRestaurante().getId()));
        }

        if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(factoryLinks.linkToUsuario(pedido.getCliente().getId()));
        }

        return pedidoModel;
    }

    public List<PedidoResumoModel> toCollectionModel(Collection<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

}