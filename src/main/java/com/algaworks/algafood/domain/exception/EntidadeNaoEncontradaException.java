package com.algaworks.algafood.domain.exception;

public class EntidadeNaoEncontradaException extends  RuntimeException{

    private static final long serilVersionUID = 1L;
    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}
