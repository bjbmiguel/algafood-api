package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)//, reason = "Entidade não encontrada!")
public class EntidadeNaoEncontradaException extends  RuntimeException{

    private static final long serilVersionUID = 1L;
    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}
