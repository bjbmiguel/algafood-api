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
import java.util.Optional;

@Service
public class CadastratarRestauranteService {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CozinhaRepository cozinhaRepository;

    public List<Restaurante> todos() {

        return restauranteRepository.findAll();
    }

    public Restaurante salvar(Restaurante restaurante) {

        Long cozinhaId = restaurante.getCozinha().getId();
        // O método "orElseThrow" retorna o object, caso contrário alguma exceção pode ser lanaçada...
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId).
                orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Não existe cadastro de cozinha com o código %d", cozinhaId)));

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }


    public Optional<Restaurante> buscar(Long restauranteId) {

        try {

            return restauranteRepository.findById(restauranteId);

        } catch (EmptyResultDataAccessException e) {

            throw new EntidadeNaoEncontradaException(String.format("Não existe cadastro de Restaurante com o código %d", restauranteId));

        }
    }

    public void excluir(Long restauranteId) {
        try {

            restauranteRepository.deleteById(restauranteId);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de Restaurante com código %d", restauranteId));

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Restaurante de código %d não pode ser removida, pois está em uso", restauranteId));
        }
    }

}
