package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class FormaDePagamentoNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public FormaDePagamentoNaoEncontradoException(String message) {
       super(message);
   }

   public FormaDePagamentoNaoEncontradoException(Long formaDePagamentoId) {
       this(String.format("Não existe um cadastro de Forma de Pagamento com o código %d", formaDePagamentoId));
   }




}
