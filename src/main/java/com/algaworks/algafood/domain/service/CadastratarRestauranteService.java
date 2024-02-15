package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CadastratarRestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CozinhaRepository cozinhaRepository;

    public List<Restaurante> todos() {

        return restauranteRepository.listar();
    }

    public Restaurante salvar(Restaurante restaurante) {

        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.porId(cozinhaId);

        if (cozinha == null) {

            throw new EntidadeNaoEncontradaException(String.format("Não existe cadastro de cozinha com o código %d", cozinhaId));
        }

        restaurante.setCozinha(cozinha);
        return restauranteRepository.salvar(restaurante);
    }


    public Restaurante buscar(Long restauranteId) {

        try {

            return restauranteRepository.porId(restauranteId);

        } catch (EmptyResultDataAccessException e) {

            throw new EntidadeNaoEncontradaException(String.format("Não existe cadastro de Restaurante com o código %d", restauranteId));

        }
    }

    public void excluir(Long restauranteId) {
        try {

            restauranteRepository.remover(restauranteId);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de Restaurante com código %d", restauranteId));

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Restaurante de código %d não pode ser removida, pois está em uso", restauranteId));
        }
    }

}
