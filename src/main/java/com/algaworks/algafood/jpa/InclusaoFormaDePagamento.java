package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;


public class InclusaoFormaDePagamento {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class).
                web(WebApplicationType.NONE) //Indica que não é uma aplicação Web
                .run(args);
        //Pegamos um bean do tipo CadastroCozinha com base no applicationContext
        FormaDePagamentoRepository formaDePagamentoRepository = applicationContext.getBean(FormaDePagamentoRepository.class);

        FormaPagamento formaDePagamento = new FormaPagamento();
        formaDePagamento.setDescricao("Cartão de crédito e débito");


        formaDePagamento = formaDePagamentoRepository.save(formaDePagamento);

        System.out.printf("%d - %s\n", formaDePagamento.getId(), formaDePagamento.getDescricao());


    }
}
