package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class EstadoRepositoryImpl implements EstadoRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<Estado> todas(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from Estado", Estado.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Estado adicionar(Estado estado){
        return manager.merge(estado); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Estado porId(Long id){
        return manager.find(Estado.class, id);
    }
    @Override
    @Transactional
    public void remover(Long estadoId){

        Estado estado = porId(estadoId);

        if(estado ==null){

            throw  new EmptyResultDataAccessException(1);
        }

        manager.remove(estado);
    }
}
