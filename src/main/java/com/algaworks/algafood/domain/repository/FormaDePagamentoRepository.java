package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FormaDePagamento;

import java.util.List;

public interface FormaDePagamentoRepository {

    List<FormaDePagamento> todas(); // Um repository tem que permitir listar todas as formas de pagamento...
    FormaDePagamento porId(Long id); // Um repository tem que permitir buscar uma as forma de pagamento pelo ID
    FormaDePagamento adicionar(FormaDePagamento formaDePagamento); // Um repository tem que permitir salvar uma  forma de pagamento
    void remover(FormaDePagamento formaDePagamento); // Um repository tem que permitir salvar uma uma  forma de pagamento
}
