package com.algaworks.algafood.di.notificacao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("notificador.email") // Prefixo
@Getter
@Setter
public class NotificadorProperties {

    /*
    * Host do servidor de email
     */
    private String hostServidor;  // host-servidor --> hostServidor

    /*
     * Porta do servidor de email
     */
    private Integer portServidor=28; // port-servidor --> portServidor
}
