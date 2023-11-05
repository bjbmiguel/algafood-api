package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class RestauranteRepositoryImpl implements RestauranteRepository {

    @PersistenceContext  // Usamos para injetar um entityManager
    private EntityManager manager; // dentre as várias funcionalidade, o entityManager traduz as nossas consutlas JPQL em SQL puro...

    @Override
    public List<Restaurante> listar(){
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return  manager.createQuery("from Restaurante", Restaurante.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Restaurante salvar(Restaurante restaurante){
        return manager.merge(restaurante); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Restaurante buscar(Long id){
        return manager.find(Restaurante.class, id);
    }
    @Override
    @Transactional
    public void remover(Restaurante restaurante){
        restaurante = buscar(restaurante.getId());
        manager.remove(restaurante);
    }
}
