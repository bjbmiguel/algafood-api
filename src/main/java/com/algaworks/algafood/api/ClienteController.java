package com.algaworks.algafood.api;

import com.algaworks.algafood.domain.model.Pedido;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ClienteController {

    @GetMapping("/clientes/v1/pedidos")
    List<Pedido> listar(){

        return Collections.EMPTY_LIST;
    }
}
