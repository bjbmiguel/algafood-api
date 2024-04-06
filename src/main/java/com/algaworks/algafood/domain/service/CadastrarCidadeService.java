package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastrarCidadeService {

    public static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removido, pós está em uso";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastrarEstadoService cadastrarEstadoService;

    public List<Cidade> listar(){
        return cidadeRepository.findAll();
    }

    public Optional<Cidade> buscar(Long estadoId) {

        try {

            return cidadeRepository.findById(estadoId);

        } catch (EmptyResultDataAccessException e) {
            throw new CidadeNaoEncontradaException(estadoId);
        }
    }
    @Transactional
    public Cidade salvar(Cidade cidade) {

        Long estadoId = cidade.getEstado().getId();
        Estado estado = cadastrarEstadoService.hasOrNot(estadoId);

        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    @Transactional
    public void excluir(Long cidadeId) {
        try {

            cidadeRepository.deleteById(cidadeId);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException
            cidadeRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new CidadeNaoEncontradaException(cidadeId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CIDADE_EM_USO, cidadeId));
        }
    }

    public Cidade hasOrNot(Long cidadeId){

        return cidadeRepository.findById(cidadeId).orElseThrow(
                () -> new CidadeNaoEncontradaException(cidadeId));
    }
}
