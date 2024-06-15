package com.algaworks.algafood.infrastructure.service.storage;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFotoStorageService implements FotoStorageService {

    public static final String USER_HOME = System.getProperty("user.home");


    @Autowired
    private StorageProperties storageProperties;

    //@Value("${algafood.storage.local.diretorio-fotos}") //Pegamos o dir a partir do file app.properties
  //  private String diretorioFotos = storageProperties.getLocal().getDiretorioFotos().toString();


    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {

        Path pathArquivo = getArquivoPath(nomeArquivo);

        try {

            if (!Files.isRegularFile(pathArquivo)) {
                throw new StorageException(String.format("O arquivo %s não existe.", nomeArquivo));
            }

            FotoRecuperada fotoRecuperada = FotoRecuperada.builder().inputStream(Files.newInputStream(pathArquivo)).build();

            return  fotoRecuperada;

        } catch (Exception e) {

            throw new StorageException("Não foi possível recuperar o arquivo.", e);
        }

    }

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {

            //Pegamos o fullPath
            Path arquivoPath = createArquivoPathIfNotExists(novaFoto.getNomeAquivo());
            //Copiamos o fluxo de entrada para um Dir... ou seja, armazenamos a foto
            FileCopyUtils.copy(novaFoto.getInputStream(),
                    Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Não foi possível armazenar arquivo.", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {

            Path arquivoPath = getArquivoPath(nomeArquivo);

            Files.deleteIfExists(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Não foi possível excluir arquivo.", e);
        }
    }

    //Juntamos o dir com o nome dda foto... fullPath
    private Path createArquivoPathIfNotExists(String nomeArquivo) {
        return getUserHome().resolve(Path.of(nomeArquivo));
    }

    private Path getArquivoPath(String nomeArquivo) {
        String diretorioFotos = storageProperties.getLocal().getDiretorioFotos().toString();
        return Path.of(USER_HOME, diretorioFotos).resolve(Path.of(nomeArquivo));
    }


    private Path getUserHome() {
        String diretorioFotos = storageProperties.getLocal().getDiretorioFotos().toString();
        Path folder = Path.of(USER_HOME, diretorioFotos);

        if (!folder.toFile().isDirectory()) {

            try {

                Files.createDirectory(folder);

            } catch (Exception e) {
                throw new StorageException("Não foi possível criar o directório.", e);
            }
        }
        return folder;
    }

}