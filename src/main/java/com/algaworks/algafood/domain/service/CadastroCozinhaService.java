package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroCozinhaService {

    public static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
    @Autowired
    private CozinhaRepository cozinhaRepository;

    //Este método apeneas delega para o repo...
    public Cozinha salvar(Cozinha cozinha) { // Vamos usar para salvar e actualizar
        return cozinhaRepository.save(cozinha);
    }

    /*public List<Cozinha> consultarPorNome(String nome) {

      return  cozinhaRepository.consultarPorNome(nome);
    }*/

    public void excluirOld(Long cozinhaId) {

        try {
            cozinhaRepository.deleteById(cozinhaId);

        } catch (EmptyResultDataAccessException e) {
            //Not Found
            throw new CozinhaNaoEncontradaException(cozinhaId);
        } catch (DataIntegrityViolationException e) {
            // Uma cozinha está em uso por um restaurante....
            throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, cozinhaId));
        }
    }

    public void excluir(Long cozinhaId) {
        try {
            if (!cozinhaRepository.existsById(cozinhaId)) {
                throw new CozinhaNaoEncontradaException(
                        cozinhaId);
            }

            cozinhaRepository.deleteById(cozinhaId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COZINHA_EM_USO, cozinhaId));
        }
    }


    public Optional<Cozinha> buscar(Long cozinhaId) {

        try {
            return cozinhaRepository.findById(cozinhaId);

        } catch (EmptyResultDataAccessException e) {
            throw new CozinhaNaoEncontradaException(cozinhaId);
        }
    }

    public Cozinha hasOrNot(Long cozinhaId) {

        return cozinhaRepository.findById(cozinhaId).orElseThrow(
                () -> new CozinhaNaoEncontradaException(
                        cozinhaId));
    }


}
