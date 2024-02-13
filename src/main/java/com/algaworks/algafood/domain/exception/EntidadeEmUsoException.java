package com.algaworks.algafood.domain.exception;

public class EntidadeEmUsoException extends RuntimeException {

    private static final long serilVersionUID = 1L;

    public EntidadeEmUsoException(String message) {
        super(message);
    }
}
