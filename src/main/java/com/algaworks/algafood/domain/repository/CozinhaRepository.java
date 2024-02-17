package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
    //O SDP irá fornecer em tempo de execução de alguns métodos como , save, delete, find, findALL

   // List<Cozinha> consultarPorNome(String nome); // Um repository tem que permitir buscar uma cozinha pelo nome...
}
