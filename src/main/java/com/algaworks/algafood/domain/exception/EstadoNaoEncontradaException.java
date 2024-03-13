package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 //podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serilVersionUID = 1L;
    public EstadoNaoEncontradaException(String message) {
        super(message);
    }

    public EstadoNaoEncontradaException(Long estadoId) {
        this(String.format("Não existe um cadastro de Estado com o código %d", estadoId));
    }

}
