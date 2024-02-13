package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public List<Restaurante> listar() {
        // Traz todos os objectos de Cozinha | Representa apenas a quary
        return manager.createQuery("from Restaurante", Restaurante.class).getResultList();
    }

    @Override
    @Transactional // Este método será executado dentro de uma transacao
    public Restaurante salvar(Restaurante restaurante) {
        return manager.merge(restaurante); //Vai retornar o objecto persistido..., ou seja, com o ID
    }

    @Override
    public Restaurante porId(Long id) {

        Restaurante restaurante = manager.find(Restaurante.class, id);

        if (restaurante != null) {

            return restaurante;
        }

        throw  new EmptyResultDataAccessException(1);
    }

    @Override
    @Transactional
    public void remover(Long restauranteId) {
        Restaurante restaurante = porId(restauranteId);
        manager.remove(restaurante);
    }
}
