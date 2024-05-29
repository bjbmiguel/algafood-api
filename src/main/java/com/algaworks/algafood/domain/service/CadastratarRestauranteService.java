package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaDePagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
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

    @Autowired
    CadastrarFormaPagamentoService cadastrarFormaPagamentoService;


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

            throw new RestauranteNaoEncontradoException(restauranteId);

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

    public Restaurante findById(Long restauranteId) {

        return restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new RestauranteNaoEncontradoException(
                        restauranteId));
    }

    public boolean existsById(Long restauranteId) {

        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RestauranteNaoEncontradoException(restauranteId); //Aqui viria a Exception que gera o código 404
        }
        return true;
    }

    public void checkIfExistsById(Long restauranteId) {

        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RestauranteNaoEncontradoException(restauranteId); //Aqui viria a Exception que gera o código 404
        }

    }

    @Transactional
    public void ativar(Long idRestaurante) {

        Restaurante restaurante = findById(idRestaurante);
        restaurante.ativar();
    }

    @Transactional
    public void inativar(Long idRestaurante){
        Restaurante restaurante = findById(idRestaurante);
        restaurante.inativar();
    }

    @Transactional
    public void abrir(Long idRestaurante){
        Restaurante restaurante = findById(idRestaurante);
        restaurante.abrir();
    }

    @Transactional
    public void fechar(Long idRestaurante){
        Restaurante restaurante = findById(idRestaurante);
        restaurante.fechar();
    }

    @Transactional
    public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = findById(restauranteId);
        FormaPagamento formaPagamento = cadastrarFormaPagamentoService.findById(formaPagamentoId);

        restaurante.removerFormaPagamento(formaPagamento);
    }

    @Transactional
    // Não precisamos chamar o método save porque o objecto restaurante está no contexto de Persistência do JPA.
    public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        Restaurante restaurante = findById(restauranteId);
        FormaPagamento formaPagamento = cadastrarFormaPagamentoService.findById(formaPagamentoId);
        restaurante.adicionarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void removerResponsavel(Restaurante restaurante, Usuario usuario) {
        restaurante.removerUsuarioResponsavel(usuario);
    }

    @Transactional
    public void adicionarResponsavel(Restaurante restaurante, Usuario usuario) {
        restaurante.adicionarUsuarioResponsavel(usuario);
    }

    @Transactional
    public void ativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::ativar);
    }

    @Transactional
    public void inativar(List<Long> restauranteIds) {
        restauranteIds.forEach(this::inativar);
    }


    public void validateFormaPagamentoById(Long restauranteId, Long formaPagamentoId) {

        Restaurante restaurante = findById(restauranteId);

        if (restaurante.validateFormaPagamentoById(formaPagamentoId).isEmpty()){

            FormaPagamento formaPagamento = cadastrarFormaPagamentoService.findById(formaPagamentoId);

            throw new FormaDePagamentoNaoEncontradoException(
                    String.format("A forma de pagamento  '%s' não é aceite por este restaurante", formaPagamento.getDescricao())
            );
        }
    }
}
