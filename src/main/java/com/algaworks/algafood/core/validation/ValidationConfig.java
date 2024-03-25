package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    @Bean //LocalValidatorFactoryBean - faz a integração do Bean Validation com o Spring
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {

        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        //Estamos costumizando uma instância de LocalValidatorFactoryBean indicano que o
        //ValidationMessageSource é o MessageSource, caso contrário, o ValidationMessages.properties será usada para resolver as mensagens do
        // Bean Validation...
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
