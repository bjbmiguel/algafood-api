package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public RestauranteNaoEncontradoException(String message) {
       super(message);
   }

   public RestauranteNaoEncontradoException(Long restauranteId) {
       this(String.format("Não existe um cadastro de Restaurante com o código %d", restauranteId));
   }



}
