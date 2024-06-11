package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Value("${algafood.storage.local.diretorio-fotos}") //Pegamos o dir a partir do file app.properties
    private Path diretorioFotos;

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {

            //Pegamos o fullPath
            Path arquivoPath = getArquivoPath(novaFoto.getNomeAquivo());

            //Copiamos o fluxo de entrada para um Dir... ou seja, armazenamos a foto
            FileCopyUtils.copy(novaFoto.getInputStream(),
                    Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Não foi possível armazenar arquivo.", e);
        }
    }

    //Juntamos o dir com o nome dda foto... fullPath
    private Path getArquivoPath(String nomeArquivo) {
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }

    //TODO implementar essa lógica de negócio, pegar a home do user e concte
    private Path getDefaultPath(){
        return Path.of(System.getProperty("user.home"));
    }

}