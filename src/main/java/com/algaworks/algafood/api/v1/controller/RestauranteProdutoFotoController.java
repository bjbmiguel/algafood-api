package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastrarProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    @Autowired
    private CadastrarProdutoService cadastroProduto;

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProduto;

    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Autowired
    private FotoStorageService fotoStorage;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Restaurantes.PodeConsultar
    public FotoProdutoModel buscar(@PathVariable Long restauranteId,
                                                  @PathVariable Long produtoId){

        FotoProduto fotoProduto = catalogoFotoProduto.findFotoByIdProdutoRestaurante(restauranteId, produtoId);

        return fotoProdutoModelAssembler.toModel(fotoProduto);
    }

    @GetMapping(produces = MediaType.ALL_VALUE)
    @CheckSecurity.Restaurantes.PodeConsultar
    public ResponseEntity<?> servir(@PathVariable Long restauranteId,
                                                          @PathVariable Long produtoId,
                                                          @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {

            FotoProduto fotoProduto = catalogoFotoProduto.findFotoByIdProdutoRestaurante(restauranteId, produtoId);
            //System.out.println(fotoProduto.getNomeArquivo());

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            FotoStorageService.FotoRecuperada fotoRecuperada = fotoStorage.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temUrl()) {
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }


        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /*
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void atualizarFotoOnlyUpload(@PathVariable Long restauranteId,
                              @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) {


        //Renomeamos a foto...
        var nomeArquivo = UUID.randomUUID().toString()
                + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();

        //Criamos um path onde a foto será armazenada
        var arquivoFoto = Path.of("/home/banguela/catalogo", nomeArquivo);

        //Just to Debug...
        System.out.println(fotoProdutoInput.getDescricao());
        System.out.println(arquivoFoto);
        System.out.println(fotoProdutoInput.getArquivo().getContentType());

        try {
            //Transferimos a foto usando o método transferTo da classe MultipartFile
            fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
*/

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
                                          @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {

        Produto produto = cadastroProduto.getProdutoByIdProdutoAndRestaurante(restauranteId, produtoId);

        MultipartFile arquivo = fotoProdutoInput.getArquivo();

        var foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto, arquivo.getInputStream());

        return fotoProdutoModelAssembler.toModel(fotoSalva);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ResponseEntity<Void> excluir(@PathVariable Long restauranteId,
                        @PathVariable Long produtoId) {

        catalogoFotoProduto.excluir(restauranteId, produtoId);

        return ResponseEntity.noContent().build();

    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto,
                                                   List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {

        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }


}
