package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaDePagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastrarFormaPagamentoService {

    public static final String MSG_FORMA_DE_PAGAMENTO_EM_USO = "Forma de Pagamento de  código %d não pode ser removida, pois está em uso";

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaDePagamento) { // Vamos usar para salvar e actualizar
        return formaDePagamentoRepository.save(formaDePagamento);
    }

    public FormaPagamento findById(Long formaDePagamentoId) {

        return formaDePagamentoRepository.findById(formaDePagamentoId).orElseThrow(
                () -> new FormaDePagamentoNaoEncontradoException(
                        formaDePagamentoId));
    }

    public Optional<FormaPagamento> buscar(Long formaDePagamentoId) {

        try {
            return formaDePagamentoRepository.findById(formaDePagamentoId);

        } catch (EmptyResultDataAccessException e) {
            throw new FormaDePagamentoNaoEncontradoException(formaDePagamentoId);
        }
    }

    @Transactional
    public void excluir(Long formaDePagamentoId) {
        try {

            if (!formaDePagamentoRepository.existsById(formaDePagamentoId)) {
                throw new FormaDePagamentoNaoEncontradoException(
                        formaDePagamentoId);
            }

            formaDePagamentoRepository.deleteById(formaDePagamentoId);
            //Para evitar a falha na captura da exceção DataIntegrityViolationException...
            formaDePagamentoRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_FORMA_DE_PAGAMENTO_EM_USO, formaDePagamentoId));
        }
    }
}
