package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.FormaDePagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class FormaDePagamentoRepositoryImpl implements FormaDePagamentoRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<FormaDePagamento> todas(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from FormaDePagamento", FormaDePagamento.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public FormaDePagamento adicionar(FormaDePagamento formaDePagamento){
        return manager.merge(formaDePagamento); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public FormaDePagamento porId(Long id){
        return manager.find(FormaDePagamento.class, id);
    }
    @Override
    @Transactional
    public void remover(FormaDePagamento formaDePagamento){
        formaDePagamento = porId(formaDePagamento.getId());
        manager.remove(formaDePagamento);
    }
}
