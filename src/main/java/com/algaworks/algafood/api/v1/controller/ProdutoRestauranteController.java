package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastrarProdutoService;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RequestMapping("/v1/restaurantes/{restauranteId}/produtos")
@RestController
public class ProdutoRestauranteController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    CadastrarProdutoService cadastrarProdutoService;

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoModelAssembler produtoModelAssembler;

    @Autowired
    ProdutoInputDisassembler produtoInputDisassembler;

    @Autowired
    FactoryLinks links;

    @GetMapping
    public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
                                                @RequestParam(required = false) Boolean incluirInativos) {

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);

        List<Produto> todosProdutos = null;

        if (Objects.nonNull(incluirInativos) && incluirInativos) {
            todosProdutos = produtoRepository.findTodosProdutosByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findTodosProdutosAtivosByRestaurante(restaurante);
        }

          return produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(links.linkToProdutos(restauranteId));
    }


    @GetMapping("/{produtoId}")
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

        Produto produto =null;

        //Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);
        if (cadastratarRestauranteService.existsById(restauranteId)) {

             produto = cadastrarProdutoService.getProdutoByIdProdutoAndRestaurante(produtoId, restauranteId);

        }



        return produtoModelAssembler.toModel(produto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId,
                               @RequestBody @Valid ProdutoInput produtoInput) {

        Restaurante restaurante = cadastratarRestauranteService.findById(restauranteId);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        return produtoModelAssembler.toModel(cadastrarProdutoService.salvar(produto));

    }

    @PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {

        Produto produtoAtual = null;

//        if (cadastratarRestauranteService.existsById(restauranteId)) {
//
//            produtoAtual = cadastrarProdutoService.findById(produtoId, restauranteId);
//
//            //copyProperties from InputModel to Entity
//            produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
//        }

        cadastratarRestauranteService.checkIfExistsById(restauranteId);
        produtoAtual = cadastrarProdutoService.getProdutoByIdProdutoAndRestaurante(produtoId, restauranteId);

        //copyProperties from InputModel to Entity
        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        return produtoModelAssembler.toModel(cadastrarProdutoService.salvar(produtoAtual));

    }



}
