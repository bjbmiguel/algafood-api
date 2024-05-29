package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serilVersionUID = 1L;

    public PermissaoNaoEncontradaException(String message) {
        super(message);
    }

    public PermissaoNaoEncontradaException(Long permisaoId) {
        this(String.format("Não existe uma cadastro de Permissão com o código %d ",
                permisaoId));
    }

    public PermissaoNaoEncontradaException(Long grupoId, Long permisaoId) {
        this(String.format("Não existe uma cadastro de Permissão com o código %d para o grupo de código %d",
                permisaoId, grupoId));
    }

}
