package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CadastratarRestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    public List<Restaurante> todos() {

        return restauranteRepository.listar();
    }

    public Restaurante buscar(Long restauranteId) {  // Será feito um bind de forma automática

        try {

            return restauranteRepository.porId(restauranteId);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de Restaurante com o código %d", restauranteId));
        }
    }

    public Restaurante salvar(Restaurante restaurante){

        return restauranteRepository.salvar(restaurante);
    }

}
