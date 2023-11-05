package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ConsultaCozinhaMain {

    public static void main(String[] args) {
        // 120 caraterres
        //Pegamos o conexto da aplicação...
        //Usamos a classe AlgafoodApiApplication para ser usada como base das nossas configurações do Spring
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);

        //Pegamos um bean do tipo CadastroCozinha com base no applicationContext
        CozinhaRepository cozinhas = applicationContext.getBean(CozinhaRepository.class);

        List<Cozinha> todasCozinhas = cozinhas.todas();

        todasCozinhas.forEach(cozinha -> {

            System.out.println("Cozinha = " + cozinha.getNome());
        });
    }
}
