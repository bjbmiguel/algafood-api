package com.algaworks.algafood.infrastructure.service.email;

import com.algaworks.algafood.core.email.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class SandBoxEnvioEmailService extends SmtpEnvioEmailService {

    private static final String ENCODE= "UTF-8";

    @Autowired
    private EmailProperties emailProperties;

    //Subscrevwmos o método criarMimeMessage de SmtpEnvioEmailService
    @Override
    protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
        MimeMessage mimeMessage = super.criarMimeMessage(mensagem); //Usamos o método criarMimeMessage  da super classe
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODE);
        helper.setTo(emailProperties.getSandbox().getDestinatario());//Passamos o destinatário como sandBox...

        return mimeMessage;
    }

}
