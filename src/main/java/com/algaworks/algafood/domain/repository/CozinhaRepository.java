package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cozinha;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {
    //O SDP irá fornecer em tempo de execução de alguns métodos como , save, delete, find, findALL
    List<Cozinha> findTodasByNomeContaining(String nome);
    Optional<Cozinha> findByNome(String nome);

    boolean existsByNome(String nome);


}
