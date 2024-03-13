package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

 //podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serilVersionUID = 1L;
    public CozinhaNaoEncontradaException(String message) {
        super(message);
    }

    public CozinhaNaoEncontradaException(Long estadoId) {
        this(String.format("Não existe um cadastro de Cozinha com o código %d", estadoId));
    }



}
