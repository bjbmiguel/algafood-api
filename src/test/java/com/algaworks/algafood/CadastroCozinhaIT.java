package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Assertions;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest // Fornece as funcionalidades do SpringBoot Test
public class CadastroCozinhaIT {

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    @Test
    public  void testarCadastroCozinhaComSucesso(){

        //cenário
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        //Acção
        novaCozinha = cadastroCozinhaService.salvar(novaCozinha);

        //Validação
        Assertions.assertNotNull(novaCozinha);
        Assertions.assertNotNull(novaCozinha.getId());
    }

    @Test
    public void testarCadastroCozinhaSemNome(){

        //cenário
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        ConstraintViolationException erroEsperado =
                Assertions.assertThrows(ConstraintViolationException.class, () -> {
                    cadastroCozinhaService.salvar(novaCozinha);
                });

        //Validação
        Assertions.assertNotNull(erroEsperado);

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso(){

        EntidadeEmUsoException erroEsperado =
                Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
                    cadastroCozinhaService.excluir(1L);
                });

        Assertions.assertNotNull(erroEsperado);

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente(){

        CozinhaNaoEncontradaException erroEsperado =
                Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
                    cadastroCozinhaService.excluir(8L);
                });

        Assertions.assertNotNull(erroEsperado);

    }



}
