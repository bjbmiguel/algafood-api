package com.algaworks.algafood.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepositoryQueries;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager; //Injetamos o Manager

    @Transactional// Para evitar que alguém chama o método fora de uma transação
    @Override
    public FotoProduto save(FotoProduto foto) {
        return manager.merge(foto); //Para inserir e atualizar
    }

}