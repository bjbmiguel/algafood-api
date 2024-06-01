package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmissaoPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    CadastrarProdutoService cadastrarProdutoService;

    @Autowired
    CadastrarFormaPagamentoService cadastrarFormaPagamentoService;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;


    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    public  Pedido findByCodigo(String codigoPedido){

        return pedidoRepository.findByCodigo(codigoPedido).orElseThrow(
                () -> new PedidoNaoEncontradoException(codigoPedido));
    }


    @Transactional
    public Pedido emitir(Pedido pedido) {
        validarPedido(pedido);
        validarItens(pedido);
        pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
        pedido.calcularValorTotal();

        return pedidoRepository.save(pedido);
    }

    private void validarItens(Pedido pedido) {
        pedido.getItens().forEach(item -> {
            Produto produto = cadastrarProdutoService.getProdutoByIdProdutoAndRestaurante(
                    pedido.getRestaurante().getId(), item.getProduto().getId());

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
        });
    }


    private void validarPedido(Pedido pedido) {
        Cidade cidade = cadastrarCidadeService.hasOrNot(pedido.getEndereco().getCidade().getId());
        Usuario cliente = cadastrarUsuarioService.findById(pedido.getCliente().getId());
        Restaurante restaurante = cadastratarRestauranteService.findById(pedido.getRestaurante().getId());
        FormaPagamento formaPagamento = cadastrarFormaPagamentoService.findById(pedido.getFormaPagamento().getId());


        pedido.getEndereco().setCidade(cidade);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setFormaPagamento(formaPagamento);

        if (restaurante.naoAceitaFormaPagamento(formaPagamento)){
            throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
                    formaPagamento.getDescricao()));
        }
    }
}
