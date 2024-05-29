package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/pedidos")
@RestController
public class PedidoController {

    @Autowired
    CadastrarPedidoService cadastrarPedidoService;

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    CadastrarProdutoService cadastrarProdutoService;

    @Autowired
    PedidoInputDisassembler pedidoInputDisassembler;

    @Autowired
    CadastrarFormaPagamentoService cadastrarFormaPagamentoService;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;


    @GetMapping
    List<PedidoResumoModel> listar() {

        return pedidoResumoModelAssembler.toCollectionModel(pedidoRepository.findAll());
    }

    @GetMapping("/{pedidoId}")
    PedidoModel findById(@PathVariable Long pedidoId) {

        return pedidoModelAssembler.toModel(cadastrarPedidoService.findById(pedidoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput) {

        cadastratarRestauranteService.validateFormaPagamentoById(pedidoInput.getRestaurante().getId(),
                pedidoInput.getFormaPagamento().getId());

        Restaurante restaurante = cadastratarRestauranteService.findById(pedidoInput.getRestaurante().getId());
        FormaPagamento formaPagamento = cadastrarFormaPagamentoService.findById(pedidoInput.getFormaPagamento().getId());

        Usuario user = cadastrarUsuarioService.findById(1L);

        Pedido pedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
        pedido.setCliente(user);
        pedido.setRestaurante(restaurante);
        pedido.setFormaPagamento(formaPagamento);
        pedido.getEndereco().setCidade(cadastrarCidadeService.hasOrNot(pedidoInput.getEndereco().getCidade().getId()));

        pedido.getItens().forEach(item -> {

            item.setProduto(cadastrarProdutoService.getProdutoByIdProdutoAndRestaurante(restaurante.getId(),
                    item.getProduto().getId()));

            item.setPedido(pedido);
            item.setPrecoUnitario(item.getProduto().getPreco());

        });

        return pedidoModelAssembler.toModel(cadastrarPedidoService.salvar(pedido));

    }

}
