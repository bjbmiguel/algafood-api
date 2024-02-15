package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Permissao;

import java.util.List;

public interface CidadeRepository {

    List<Cidade> todas();
    Cidade buscar(Long id);
    Cidade adicionar(Cidade cidade);
    void remover(Long cidadeId);
}
