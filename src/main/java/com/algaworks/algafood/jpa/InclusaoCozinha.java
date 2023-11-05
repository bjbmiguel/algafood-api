package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class InclusaoCozinha {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean do tipo CadastroCozinha com base no applicationContext
        CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);

        Cozinha cozinha = new Cozinha();
        cozinha.setNome("Brasileira");

        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("Japonesa");

        cozinha = cozinhaRepository.adicionar(cozinha);
        cozinha1 = cozinhaRepository.adicionar(cozinha1);

        System.out.printf("%d - %s\n", cozinha.getId(), cozinha.getNome());
        System.out.printf("%d - %s\n", cozinha1.getId(), cozinha1.getNome());

    }
}
