package com.algaworks.algafood.domain.exception;

//podemos remover ou não o código NOT_FOUND porque ele já está mapeado na EntidadeNaoEncontradaException
// Podemos tratar, uma específica (estado nao encontardo..) ou mais genérica (entidade não encontrada...)
public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaException {

   private static final long serilVersionUID = 1L;
   public FotoProdutoNaoEncontradaException(String message) {
       super(message);
   }

   public FotoProdutoNaoEncontradaException(Long fotoId) {
       this(String.format("Não existe um cadastro de Foto com o código %d", fotoId));
   }

    public FotoProdutoNaoEncontradaException(Long produtoId, Long restuaranteId) {
        this(String.format("Não existe um cadastro de Foto do Produto com código %d para o Restaurante com código %d",
                produtoId, restuaranteId));
    }



}
