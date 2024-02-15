package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class CidadeRepositoryImpl implements CidadeRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<Cidade> todas(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from Cidade", Cidade.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Cidade adicionar(Cidade cidade){
        return manager.merge(cidade); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Cidade buscar(Long id){
        return manager.find(Cidade.class, id);
    }
    @Transactional
    @Override
    public void remover(Long id) {
        Cidade cidade = buscar(id);

        if (cidade == null) {
            throw new EmptyResultDataAccessException(1);
        }

        manager.remove(cidade);
    }

}
