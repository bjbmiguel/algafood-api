package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FormaDePagamanetoInputDisassembler;
import com.algaworks.algafood.api.assembler.FormaDePagamentoModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.FormaDePagamentoModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.api.model.input.FormaDePagamantoInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaDePagamento;
import com.algaworks.algafood.domain.repository.FormaDePagamentoRepository;
import com.algaworks.algafood.domain.service.FormaDePagamentoService;
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
    private FormaDePagamentoService formaDePagamentoService;
    @Autowired
    private FormaDePagamentoModelAssembler formaDePagamentoModelAssembler;
    @Autowired
    private FormaDePagamanetoInputDisassembler formaDePagamanetoInputDisassembler;

    @GetMapping
    public List<FormaDePagamentoModel> listar() {

        return formaDePagamentoModelAssembler.toCollectionModel(formaDePagamentoRepository.findAll());
    }

    @GetMapping(value = "/{formaDePagamentoId}")
    public FormaDePagamentoModel buscar(@PathVariable Long formaDePagamentoId) {  // Será feito um bind de forma automática

        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.hasOrNot(formaDePagamentoId));
    }

    @PostMapping // Usamos a anotação @PostMapping que é um mapeamento do método POST HTTP
    @ResponseStatus(HttpStatus.CREATED) //Costumizamos o status da resposta... para 201
    public FormaDePagamentoModel adicionar(@RequestBody @Valid FormaDePagamantoInput formaDePagamantoInput) { //Anotamos o parâmetro "cozinha"
        FormaDePagamento formaDePagamento = formaDePagamanetoInputDisassembler.toDomainObject(formaDePagamantoInput);
        return formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));
    }

    @PutMapping(value = "/{formaDePagamentoId}")
    public FormaDePagamentoModel actualizar(@RequestBody @Valid FormaDePagamantoInput formaDePagamantoInput, @PathVariable Long formaDePagamentoId) {

        FormaDePagamento formaDePagamento = formaDePagamentoService.hasOrNot(formaDePagamentoId);
        formaDePagamanetoInputDisassembler.copyToDomainObject(formaDePagamantoInput, formaDePagamento);
        return  formaDePagamentoModelAssembler.toModel(formaDePagamentoService.salvar(formaDePagamento));

    }

    @DeleteMapping(value = "/{formaDePagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaDePagamentoId) {

        formaDePagamentoService.excluir(formaDePagamentoId);

    }

}
