package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastrarGrupoService {

    public static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, pós está em uso";
    @Autowired //Injetamos o componente  EstadoRepository...
    GrupoRepository grupoRepository;


    public List<Grupo> listar() {
        return grupoRepository.findAll();
    }

    public Optional<Grupo> buscar(Long grupoId) {

        try {

            return grupoRepository.findById(grupoId);

        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(grupoId);
        }
    }


    @Transactional
    public Grupo salvar(Grupo grupo) { //Vamos usar para salvar e actualizar
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void excluir(Long grupoId) {

        try {

            grupoRepository.deleteById(grupoId);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException
            grupoRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            //Not Found
            throw new GrupoNaoEncontradoException(grupoId);
        } catch (DataIntegrityViolationException e) {
            // Uma cozinha está em uso por um restaurante....
            throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
        }
    }

    //Retorna um objecto do tipo cozinha na resposta, caso contrário uma resposta com código 404 será lançada...
    public Grupo findById(Long grupoId) {

        return grupoRepository.findById(grupoId).orElseThrow(
                () -> new GrupoNaoEncontradoException(
                        grupoId));
    }

    @Transactional
    public void removerPermissao(Grupo grupo, Permissao permissao) {
        grupo.removerPermissao(permissao);
    }

    @Transactional
    public void adicionarPermissao(Grupo grupo, Permissao permissao) {
        grupo.adicionarPermissao(permissao);
    }

    public boolean existById(Long grupoId){

        if (!grupoRepository.existsById(grupoId)) {
            throw new GrupoNaoEncontradoException(
                    grupoId);
        }

        return true;
    }


    public void checkIfExistById(Long grupoId){

        if (!grupoRepository.existsById(grupoId)) {
            throw new GrupoNaoEncontradoException(
                    grupoId);
        }

    }

}
