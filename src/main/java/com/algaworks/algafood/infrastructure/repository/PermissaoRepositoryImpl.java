package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.FormaDePagamento;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class PermissaoRepositoryImpl implements PermissaoRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<Permissao> todas(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from Permissao", Permissao.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Permissao adicionar(Permissao permissao){
        return manager.merge(permissao); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Permissao porId(Long id){
        return manager.find(Permissao.class, id);
    }
    @Override
    @Transactional
    public void remover(Permissao permissao){
        permissao = porId(permissao.getId());
        manager.remove(permissao);
    }
}
