package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;


public class InclusaoEstado {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean  com base no applicationContext
        EstadoRepository estadoRepository = applicationContext.getBean(EstadoRepository.class);

        Estado estado = new Estado();
        estado.setNome("Benguela");
        estado = estadoRepository.adicionar(estado);

        System.out.printf("%d - %s\n", estado.getId(), estado.getNome());


    }
}
