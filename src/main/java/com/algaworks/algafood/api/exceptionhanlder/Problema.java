package com.algaworks.algafood.api.exceptionhanlder;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder //usamos para construir objectos de forma mais fluente...
@Getter
public class Problema {

    private LocalDateTime dateTime;
    private String mensagem;
}
