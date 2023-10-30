package com.algaworks.algafood.di.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmissaoNotaFiscalService {

    @EventListener // Indica que este método é um ouvinte (listener) de  ClienteAtivadoEvent
    public void clienteAtivadoListener(ClienteAtivadoEvent event) {
        System.out.println("Emitindo nota fiscal para o cliente " + event.getCliente());

    }

}
