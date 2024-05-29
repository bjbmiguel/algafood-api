package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public UsuarioNaoEncontradoException(String message) {
       super(message);
   }

   public UsuarioNaoEncontradoException(Long usuarioId) {
       this(String.format("Não existe um cadastro de Usuário com o código %d", usuarioId));
   }

    public UsuarioNaoEncontradoException(Long usuarioId, Long restauranteId) {
        this(String.format("Não existe um cadastro de Usuário com o código %d associado " +
                "ao Restaurante de código %d", usuarioId, restauranteId));
    }


}
