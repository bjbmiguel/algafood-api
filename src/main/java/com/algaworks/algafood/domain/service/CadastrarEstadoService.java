package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastrarEstadoService {

    public static final String MSG_ESATDO_EM_USO = "Estado de código %d não pode ser removido, pós está em uso";
    @Autowired //Injetamos o componente  EstadoRepository...
    EstadoRepository estadoRepository;

    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    public Optional<Estado> buscar(Long estadoId) {

        try {
            return estadoRepository.findById(estadoId);

        } catch (EmptyResultDataAccessException e) {
            throw new EstadoNaoEncontradaException(estadoId);
        }
    }


    @Transactional
    public Estado salvar(Estado estado) { //Vamos usar para salvar e actualizar
        return estadoRepository.save(estado);
    }

    @Transactional
    public void excluir(Long estadoId) {

        try {

            estadoRepository.deleteById(estadoId);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException
            estadoRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            //Not Found
            throw new EstadoNaoEncontradaException(estadoId);
        } catch (DataIntegrityViolationException e) {
            // Uma cozinha está em uso por um restaurante....
            throw new EntidadeEmUsoException(String.format(MSG_ESATDO_EM_USO, estadoId));
        }
    }


    //Retorna um objecto do tipo cozinha na resposta, caso contrário uma resposta com código 404 será lançada...
    public Estado hasOrNot(Long estadoId) {

        return estadoRepository.findById(estadoId).orElseThrow(
                () -> new EstadoNaoEncontradaException(
                        estadoId));
    }
}
