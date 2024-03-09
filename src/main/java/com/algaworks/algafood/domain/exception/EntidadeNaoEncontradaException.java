package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntidadeNaoEncontradaException extends ResponseStatusException {

    private static final long serilVersionUID = 1L;

    public EntidadeNaoEncontradaException(String msn){
        this(HttpStatus.NOT_FOUND, msn);
    }
    public EntidadeNaoEncontradaException(HttpStatus status, String mensagem) {
        super(status, mensagem);
    }

}
