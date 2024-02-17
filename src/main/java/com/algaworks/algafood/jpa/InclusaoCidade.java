package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;


public class InclusaoCidade {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean  com base no applicationContext
        CidadeRepository cidadeRepository = applicationContext.getBean(CidadeRepository.class);



        Estado estado = new Estado();
        estado.setId(1L);

        Cidade cidade = new Cidade();
        cidade.setEstado(estado);
        cidade.setNome("Cacucao");
        cidade =  cidadeRepository.save(cidade);


        System.out.printf("%d - %s\n", cidade.getId(), cidade.getNome());


    }
}
