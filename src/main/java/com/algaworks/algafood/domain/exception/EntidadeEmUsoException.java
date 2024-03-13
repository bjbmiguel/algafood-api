package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EntidadeEmUsoException extends NegocioException {

    private static final long serilVersionUID = 1L;

    public EntidadeEmUsoException(String message) {
        super(message);
    }
}
