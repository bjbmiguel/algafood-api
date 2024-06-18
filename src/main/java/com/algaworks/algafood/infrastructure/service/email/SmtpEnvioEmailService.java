package com.algaworks.algafood.infrastructure.service.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;


public class SmtpEnvioEmailService implements EnvioEmailService {

    private static final String ENCODE= "UTF-8";

    @Autowired
    private JavaMailSender mailSender; //Do pacote Spring, usada para criar uma inst de MimeMessage

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freemarkerConfig;


    @Override
    public void enviar(Mensagem mensagem) {

      try {

          String corpo = processarTemplate(mensagem);

          MimeMessage mimeMessage = mailSender.createMimeMessage();//Representa a mensagem que queremos enviar...
          MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODE);//Auxiliar a configurar o mimeMessage de forma fácil..
          helper.setSubject(mensagem.getAssunto());
          helper.setText(corpo, true);//O segundo parame, boolean, representa um corpo html
          //Na conversao de qualquer collec para Array é necessário passar um novo Array vazio como parâmetro
          helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
          helper.setFrom(emailProperties.getRemetente());

          mailSender.send(mimeMessage);

      }catch (Exception e){//Pegamos qualquer exception...

          throw new EmailException("Não foi possível enviar e-mail", e);
      }
    }


    protected String processarTemplate(Mensagem mensagem) {
        try {
            Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());

            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    template, mensagem.getVariaveis());
        } catch (Exception e) {
            throw new EmailException("Não foi possível montar o template do e-mail", e);
        }
    }
}
