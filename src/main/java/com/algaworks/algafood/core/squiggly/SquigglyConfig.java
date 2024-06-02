package com.algaworks.algafood.core.squiggly;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

/*
O Squiggly não está mais sendo mantido e foi depreciado pelo seu autor: https://github.com/bohnman/squiggly.
Logo, ele não irá funcionar corretamente ao utilizar as versões mais recentes do Spring.
Ele ainda pode ser utilizado para realizar a aula, porém o seu uso em produção é desencorajado.
 */

//Criamos um filtro global para as nossas requisições HTTP
@Configuration
public class SquigglyConfig {

    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper) {
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));

        var urlPatterns = Arrays.asList("/cozinhas/*", "/pedidos/*", "/restaurantes/*"); //Limitamos os filtro somente para as url's ...

        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
        filterRegistration.setFilter(new SquigglyRequestFilter());
        filterRegistration.setOrder(1);
        filterRegistration.setUrlPatterns(urlPatterns);

        return filterRegistration;
    }

}