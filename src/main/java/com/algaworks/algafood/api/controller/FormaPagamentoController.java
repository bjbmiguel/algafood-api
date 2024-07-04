package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FormaDePagamanetoInputDisassembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaDePagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.service.CadastrarFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/formas-de-pagamentos")
@RestController
public class FormaPagamentoController {

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;
    @Autowired
    private CadastrarFormaPagamentoService formaDePagamentoService;
    @Autowired
    private FormaPagamentoModelAssembler formaDePagamentoModelAssembler;
    @Autowired
    private FormaDePagamanetoInputDisassembler formaDePagamanetoInputDisassembler;


    @GetMapping
    public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaDePagamentoRepository.getDataUltimaAtualizacao();

        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        List<FormaPagamento> todasFormasPagamentos = formaDePagamentoRepository.findAll();

        List<FormaPagamentoModel> formasPagamentosModel = formaDePagamentoModelAssembler
                .toCollectionModel(todasFormasPagamentos);

        return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
//				.cacheControl(CacheControl.noCache())
                //             .cacheControl(CacheControl.noStore())
                .body(formasPagamentosModel);
    }

    @GetMapping(value = "/{formaDePagamentoId}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaDePagamentoId) {  // Será feito um bind de forma automática

        var formaPagamento = formaDePagamentoModelAssembler.toModel(formaDePagamentoService.findById(formaDePagamentoId));

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(formaPagamento);
    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaDePagamentoInput formaDePagamantoInput) { //Anotamos o parâmetro "cozinha"
        FormaPagamento formaDePagamento = formaDePagamanetoInputDisassembler.toDomainObject(formaDePagamantoInput);
        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));
    }

    @PutMapping(value = "/{formaDePagamentoId}")
    public FormaPagamentoModel actualizar(@RequestBody @Valid FormaDePagamentoInput formaDePagamantoInput, @PathVariable Long formaDePagamentoId) {

        FormaPagamento formaDePagamento = formaDePagamentoService.findById(formaDePagamentoId);
        formaDePagamanetoInputDisassembler.copyToDomainObject(formaDePagamantoInput, formaDePagamento);
        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));

    }

    @DeleteMapping(value = "/{formaDePagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaDePagamentoId) {

        formaDePagamentoService.excluir(formaDePagamentoId);

    }

}
