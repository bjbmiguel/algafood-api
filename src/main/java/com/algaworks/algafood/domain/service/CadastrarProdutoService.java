package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastrarProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto) {

        return produtoRepository.save(produto);

    }


    public Produto getProdutoByIdProdutoAndRestaurante(Long restauranteId, Long produtoId) {

        return validateProdutoByIdProdutoAndRestaurante(restauranteId, produtoId);
    }

    public Produto validateProdutoByIdProdutoAndRestaurante(Long restauranteId, Long produtoId) {

        return produtoRepository.validateProdutoByIdProdutoAndRestaurante(restauranteId, produtoId).orElseThrow(
                () -> new ProdutoNaoEncontradoException(
                        restauranteId, produtoId));
    }


}
