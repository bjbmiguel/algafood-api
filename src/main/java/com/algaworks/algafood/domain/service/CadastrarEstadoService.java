package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastrarEstadoService {

    @Autowired //Injetamos o componente  EstadoRepository...
    EstadoRepository estadoRepository;

    public List<Estado> listar(){
        return estadoRepository.todas();
    }

    public Estado buscar(Long estadoId) {

        try {

            return estadoRepository.porId(estadoId);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de Estado com o código %d", estadoId));
        }
    }

    public Estado salvar(Estado estado) { //Vamos usar para salvar e actualizar
        return estadoRepository.adicionar(estado);
    }

    public void excluir(Long estadoId) {

        try {

            estadoRepository.remover(estadoId);

        }catch (EmptyResultDataAccessException e){
            //Not Found
            throw  new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de Estado com o código %d", estadoId));
        } catch (DataIntegrityViolationException e) {
            // Uma cozinha está em uso por um restaurante....
            throw new EntidadeEmUsoException(String.format("Cozinha de código %d não pode ser Estado, pós está em uso", estadoId));
        }
    }
}
