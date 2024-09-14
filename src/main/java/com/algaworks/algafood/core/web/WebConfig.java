package com.algaworks.algafood.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiDeprecationHandler apiDeprecationHandler; //Usado para adicionar um header notificando que a API será depreciada..

    @Autowired
    ApiRetirementHandler apiRetirementHandler; //Usando para alteração as resposta das requisições para 410 GONE



   /*

  Comentamos por que vamos usar o versionamento por uri e n mais por media type
    //Definimos o content type padrão...
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(AlgaMediaTypes.V2_APPLICATION_JSON);
    }

    */

    //Filtro para interceptar as respostas das requisições e calcular hash e adiciona um cabeçalho Etag...
    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    //Filtro para interceptar as requisições e alterar um header
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRetirementHandler);
    }

}
