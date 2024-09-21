package com.algaworks.algafood.infrastructure.service.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class SmtpEnvioEmailService implements EnvioEmailService {

    private static final String ENCODE= "UTF-8";

    @Autowired
    private JavaMailSender mailSender; //Do pacote Spring, usada para criar uma inst de MimeMessage

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private ProcessadorEmailTemplate processadorEmailTemplate;


    @Override
    public void enviar(Mensagem mensagem) {

      try {

          MimeMessage mimeMessage = criarMimeMessage(mensagem);
          mailSender.send(mimeMessage);

      }catch (Exception e){//Pegamos qualquer exception...
          throw new EmailException("Não foi possível enviar e-mail", e);
      }
    }

    //Usamo o modificador de acesso "protected" para permitir o acesso a  classes que somente fazem parte do mesmo pacote
    protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
        String corpo = processadorEmailTemplate.processarTemplate(mensagem);
        MimeMessage mimeMessage = mailSender.createMimeMessage();//Representa a mensagem que queremos enviar...

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ENCODE);//Auxiliar a configurar o mimeMessage de forma fácil..
        helper.setFrom(emailProperties.getRemetente()); //Remetente
        helper.setTo(mensagem.getDestinatarios().toArray(new String[0])); //Na conversao de qualquer collec para Array é necessário passar um novo Array vazio como parâmetro
        helper.setSubject(mensagem.getAssunto());
        helper.setText(corpo, true);//O segundo parame, boolean, representa um corpo html

        return mimeMessage;
    }



}
