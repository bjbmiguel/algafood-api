package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.FormaDePagamento;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;


public class InclusaoPermissao {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean  com base no applicationContext
        PermissaoRepository permissaoRepository = applicationContext.getBean(PermissaoRepository.class);

        Permissao permissao = new Permissao();
        permissao.setNome("Cadastrar Cozinha");
        permissao.setDescricao("Usuário com permissão de cadastrar uma cozinha.");


        permissao = permissaoRepository.adicionar(permissao);

        System.out.printf("%d - %s\n", permissao.getId(), permissao.getNome());


    }
}
