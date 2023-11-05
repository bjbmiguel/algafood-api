package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<Cozinha> todas(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from Cozinha", Cozinha.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Cozinha adicionar(Cozinha cozinha){
        return manager.merge(cozinha); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Cozinha porId(Long id){
        return manager.find(Cozinha.class, id);
    }
    @Override
    @Transactional
    public void remover(Cozinha cozinha){
        cozinha = porId(cozinha.getId());
        manager.remove(cozinha);
    }

}
