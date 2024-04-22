package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FormaDePagamanetoInputDisassembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaDePagamantoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.service.CadastrarFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/formas-de-pagamentos")
@RestController
public class FormaDePagamentoController {

    @Autowired
    private FormaDePagamentoRepository formaDePagamentoRepository;
    @Autowired
    private CadastrarFormaPagamentoService formaDePagamentoService;
    @Autowired
    private FormaPagamentoModelAssembler formaDePagamentoModelAssembler;
    @Autowired
    private FormaDePagamanetoInputDisassembler formaDePagamanetoInputDisassembler;

    @GetMapping
    public List<FormaPagamentoModel> listar() {

        return formaDePagamentoModelAssembler.toCollectionModel(formaDePagamentoRepository.findAll());
    }

    @GetMapping(value = "/{formaDePagamentoId}")
    public FormaPagamentoModel buscar(@PathVariable Long formaDePagamentoId) {  // Será feito um bind de forma automática

        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.hasOrNot(formaDePagamentoId));
    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaDePagamantoInput formaDePagamantoInput) { //Anotamos o parâmetro "cozinha"
        FormaPagamento formaDePagamento = formaDePagamanetoInputDisassembler.toDomainObject(formaDePagamantoInput);
        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));
    }

    @PutMapping(value = "/{formaDePagamentoId}")
    public FormaPagamentoModel actualizar(@RequestBody @Valid FormaDePagamantoInput formaDePagamantoInput, @PathVariable Long formaDePagamentoId) {

        FormaPagamento formaDePagamento = formaDePagamentoService.hasOrNot(formaDePagamentoId);
        formaDePagamanetoInputDisassembler.copyToDomainObject(formaDePagamantoInput, formaDePagamento);
        return  formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));

    }

    @DeleteMapping(value = "/{formaDePagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaDePagamentoId) {

        formaDePagamentoService.excluir(formaDePagamentoId);

    }

}
