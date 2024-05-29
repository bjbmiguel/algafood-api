package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.model.Restaurante;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public ProdutoNaoEncontradoException(String message) {
       super(message);
   }

   public ProdutoNaoEncontradoException(Long restaurante,Long produtoId) {
       this(String.format("Não existe um cadastro de Produto com o código %d para o restaurante de código %d",
               produtoId, restaurante));
   }

}
