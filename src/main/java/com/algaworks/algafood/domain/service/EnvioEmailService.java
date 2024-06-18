package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem);

    @Builder
    @Getter
    class Mensagem{ //Classe aninhada

        @Singular
        private Set<String> destinatarios;

        @NotNull
        private String assunto;

        @NotNull
        private  String corpo;

        @Singular("variavel")
        private Map<String, Object> variaveis;
    }
}
