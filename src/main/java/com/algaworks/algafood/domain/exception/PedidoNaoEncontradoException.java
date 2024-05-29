package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public PedidoNaoEncontradoException(String message) {
       super(message);
   }

   public PedidoNaoEncontradoException(Long pedidoId, Long restaurante) {
       this(String.format("Não existe um cadastro de Pedido com o código %d para o restaurante de código %d",
               pedidoId, restaurante));
   }

    public PedidoNaoEncontradoException(Long pedidoId) {
        this(String.format("Não existe um cadastro de Pedido com o código %d",
                pedidoId));
    }

}
