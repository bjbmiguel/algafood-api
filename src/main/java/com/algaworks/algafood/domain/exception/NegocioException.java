package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public  class NegocioException extends  RuntimeException{ //Exceção de negócio mais genérica, qualquer coisa relacionada com o domínio...

    public NegocioException(String message) {
        super(message);
    }
    public NegocioException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
