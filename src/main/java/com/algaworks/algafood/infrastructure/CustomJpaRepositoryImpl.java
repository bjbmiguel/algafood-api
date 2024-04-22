package com.algaworks.algafood.infrastructure;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
        implements CustomJpaRepository<T, ID> {

    private EntityManager manager;
    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager); // Chama o construtor da classe Pai "SimpleJpaRepository"
        this.manager = entityManager;
    }
    @Override
    public Optional<T> buscarPrimeiro() {

        var jpql = "from " + getDomainClass().getName();

        T entity = manager.createQuery(jpql, getDomainClass())
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(entity);
    }

    @Override
    public void detach(T entity) { // Usamos este método para desanexar uma entidade do contexto de persistência do JPA.

        manager.detach(entity);
    }


}
