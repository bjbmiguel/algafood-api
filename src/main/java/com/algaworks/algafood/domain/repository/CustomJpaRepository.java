package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean  // Esta anotação indica ao Spring que esta repositório n será instanciado... será usado para fins
// de herança
public interface CustomJpaRepository<T, D> extends JpaRepository<T, D> {
    // T - Representa a classe e o D -- Representa o ID da classe ...
    Optional<T> buscarPrimeiro();
}
