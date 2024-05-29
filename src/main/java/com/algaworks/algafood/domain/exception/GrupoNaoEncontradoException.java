package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public GrupoNaoEncontradoException(String message) {
       super(message);
   }

   public GrupoNaoEncontradoException(Long grupoId) {
       this(String.format("Não existe um cadastro de Grupo com o código %d", grupoId));
   }

    public GrupoNaoEncontradoException(Long grupoId, Long usuarioId) {
        this(String.format("Não existe um cadastro de Grupo com o código %d associado ao usuário com código %d",
                grupoId, usuarioId));
    }



}
