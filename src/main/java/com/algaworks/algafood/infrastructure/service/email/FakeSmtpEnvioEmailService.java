package com.algaworks.algafood.infrastructure.service.email;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FakeSmtpEnvioEmailService extends SmtpEnvioEmailService {

    @Override
    public void enviar(Mensagem mensagem) {

        try {

            String corpo = processarTemplate(mensagem);
            log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);

        }catch (Exception e){

            throw new EmailException("Não foi possível enviar e-mail", e);
        }

    }


}
