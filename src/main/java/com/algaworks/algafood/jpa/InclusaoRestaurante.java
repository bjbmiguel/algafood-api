package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public class InclusaoRestaurante {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean do tipo CadastroCozinha com base no applicationContext
        RestauranteRepository cozinhaRepository = applicationContext.getBean(RestauranteRepository.class);
        Cozinha cozinha = new Cozinha();
        cozinha.setId(1L);
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Café Delmar");
        restaurante.setCozinha(cozinha);
        restaurante.setTaxaFrete(new BigDecimal("10.50"));


        restaurante = cozinhaRepository.salvar(restaurante);

        System.out.printf("%d - %s\n", restaurante.getId(), restaurante.getNome());


    }
}
