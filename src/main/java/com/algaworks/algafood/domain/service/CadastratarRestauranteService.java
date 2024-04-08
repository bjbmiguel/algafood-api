package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class CadastratarRestauranteService {

    public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removido, pós está em uso";

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CozinhaRepository cozinhaRepository;

    @Autowired
    CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;

    public List<Restaurante> todos() {

        return restauranteRepository.findAll();
    }

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {

        Long cozinhaId = restaurante.getCozinha().getId();
        Long cidadeId = restaurante.getEndereco().getCidade().getId();

        Cozinha cozinha = cadastroCozinhaService.hasOrNot(cozinhaId);
        Cidade cidade = cadastrarCidadeService.hasOrNot(cidadeId);

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);

        return restauranteRepository.save(restaurante);

    }


    public Optional<Restaurante> buscar(Long restauranteId) {

        try {

            return restauranteRepository.findById(restauranteId);

        } catch (EmptyResultDataAccessException e) {

            throw new RestauranteNaoEncontradoException( restauranteId);

        }
    }

    @Transactional
    public void excluir(Long restauranteId) {
        try {

            restauranteRepository.deleteById(restauranteId);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException
            restauranteRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new RestauranteNaoEncontradoException(
                    restauranteId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_RESTAURANTE_EM_USO, restauranteId));
        }
    }

    public Restaurante hasOrNot(Long restauranteId) {

        return restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new RestauranteNaoEncontradoException(
                        restauranteId));
    }

    @Transactional
    public void ativar(Long idRestaurante){

        Restaurante restaurante = hasOrNot(idRestaurante);
        restaurante.ativar();
    }

    @Transactional
    public void inativar(Long idRestaurante){

        Restaurante restaurante = hasOrNot(idRestaurante);
        restaurante.inativar();
    }

}
