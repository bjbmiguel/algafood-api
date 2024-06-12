package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream inputStream){

        Long restauranteId = foto.getRestauranteId();
        Long produtoId = foto.getProduto().getId();
        String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;

        Optional<FotoProduto> fotoExistente = produtoRepository
                .findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            produtoRepository.delete(fotoExistente.get());
        }
        //Salvamos primeiro os dados da foto no Banco de Dados
        foto.setNomeArquivo(nomeNovoArquivo);//Passamos o novo nome da foto...
        foto = produtoRepository.save(foto);
        produtoRepository.flush(); //Pedimos que seja feita uma descarga no Banco de Dados...

        FotoStorageService.NovaFoto novaFoto = FotoStorageService.NovaFoto.builder()
                .nomeAquivo(foto.getNomeArquivo())
                .inputStream(inputStream) //Recebemos o fluxo de dados do arquivo..
                .build();
        //Armazenamos agora a foto Ser uma exceção for lançada um rollback será feito no Banco de Dados...
        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
        return foto;
    }

}