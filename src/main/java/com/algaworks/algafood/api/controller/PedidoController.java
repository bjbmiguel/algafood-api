package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/pedidos")
@RestController
public class PedidoController {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

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

    /*
    @GetMapping
    List<PedidoResumoModel> listar() {

        return pedidoResumoModelAssembler.toCollectionModel(pedidoRepository.findAll());
    }


     */

    @GetMapping("/{codigoPedido}")
    PedidoModel findById(@PathVariable String codigoPedido) {

        return pedidoModelAssembler.toModel(emissaoPedidoService.findByCodigo(codigoPedido));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput) {

        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedidoService.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

}
