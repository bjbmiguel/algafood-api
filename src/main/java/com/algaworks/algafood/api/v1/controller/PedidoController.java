package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import com.algaworks.algafood.domain.service.*;
import com.algaworks.algafood.infrastructure.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/v1/pedidos")
@RestController
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    CadastrarProdutoService cadastrarProdutoService;

    @Autowired
    PedidoInputDisassembler pedidoInputDisassembler;

    @Autowired
    CadastrarFormaPagamentoService cadastrarFormaPagamentoService;

    @Autowired
    CadastrarUsuarioService cadastrarUsuarioService;

    @Autowired
    CadastrarCidadeService cadastrarCidadeService;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @Autowired
    private AlgaSecurity algaSecurity;

    //Pesquisa usando Specification...
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Pedidos.PodePesquisar
    @Override
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter, @PageableDefault(size = 20) Pageable pageable) {

       var  pageableTraduzido = traduzirPageable(pageable);//Traduzimos um pageable mapeando as propriedades do sort...

        Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        PagedModel<PedidoResumoModel> pedidosPagedModel = pagedResourcesAssembler
                .toModel(pedidosPage, pedidoResumoModelAssembler);

        return pedidosPagedModel;
    }

    /*


    @GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoResumoModel> pedidoResumoModels = pedidoResumoModelAssembler.toCollectionModel(pedidos);

        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidoResumoModels);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

        //Serializamos todos os campos por default...
        simpleFilterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        //Se tiver algum campo filtrar somente aqueles campos na serialização do object
        if(StringUtils.isNotBlank(campos)){
            //Adicionamos o nosso filter, serializamos todas as propriedades
            simpleFilterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(
                    campos.split(",") //Quebra a string separada por vírgula em um array...
            ));

        }
        //Adicionamos em pedidosWrapper o filtro
        pedidosWrapper.setFilters(simpleFilterProvider);
        return pedidosWrapper;
    }
     */

    /*
    @GetMapping
    List<PedidoResumoModel> listar() {

        return pedidoResumoModelAssembler.toCollectionModel(pedidoRepository.findAll());
    }


     */

    @GetMapping(path = "/{codigoPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckSecurity.Pedidos.PodeBuscar
    @Override
    public PedidoModel buscar(@PathVariable String codigoPedido) {

        return pedidoModelAssembler.toModel(emissaoPedidoService.findByCodigo(codigoPedido));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput) {

        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            //TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(algaSecurity.getUsuarioId());

            novoPedido = emissaoPedidoService.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable apiPageable) {

        /*
        var mapeamento = ImmutableMap.of(
                "codigo", "codigo",
                "restaurante.nome", "restaurante.nome",
                "nomeCliente", "cliente.nome",
                "valorTotal", "valorTotal",
                "taxaFrete", "taxaFrete"
        );

         */

        ImmutableMap<String, String> mapeamento = ImmutableMap.<String, String>builder()
                .put("codigo", "codigo")
                .put("nomerestaurante", "restaurante.nome")
                .put("nomeCliente", "cliente.nome")
                .put("valorTotal", "valorTotal")
                .put("taxaFrete", "taxaFrete")
                .put("subtotal", "subtotal")
                .build();

        return PageableTranslator.translate(apiPageable, mapeamento);
    }


}
