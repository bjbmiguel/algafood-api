package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public CidadeNaoEncontradaException(String message) {
       super(message);
   }

   public CidadeNaoEncontradaException(Long estadoId) {
       this(String.format("Não existe um cadastro de Cidade com o código %d", estadoId));
   }



}
