package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class GrupoNaoEncontradaException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public GrupoNaoEncontradaException(String message) {
       super(message);
   }

   public GrupoNaoEncontradaException(Long grupoId) {
       this(String.format("Não existe um cadastro de Grupo com o código %d", grupoId));
   }



}
