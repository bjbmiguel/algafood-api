package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class EntidadeNaoEncontradaException extends NegocioException {

    private static final long serilVersionUID = 1L;

    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}
