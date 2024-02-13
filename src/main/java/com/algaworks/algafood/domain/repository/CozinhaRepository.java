package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cozinha;

import java.util.List;

public interface CozinhaRepository {

    List<Cozinha> todas(); // Um repository tem que permitir listar todas as cozinhas...
    Cozinha porId(Long id); // Um repository tem que permitir buscar uma cozinha pelo ID
    Cozinha adicionar(Cozinha cozinha); // Um repository tem que permitir salvar uma cozinha
    void remover(Long cozinhaId); // Um repository tem que permitir salvar uma cozinha
}
