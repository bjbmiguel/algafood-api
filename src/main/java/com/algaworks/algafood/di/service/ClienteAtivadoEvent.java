package com.algaworks.algafood.di.service;

import com.algaworks.algafood.di.modelo.Cliente;
import lombok.Getter;

@Getter
public class ClienteAtivadoEvent {

    private Cliente cliente;

    public ClienteAtivadoEvent(Cliente cliente) {
        this.cliente = cliente;
    }

}
