package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.model.input.FormaDePagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaDePagamanetoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toDomainObject(FormaDePagamentoInput formaDePagamantoInput) {
        return modelMapper.map(formaDePagamantoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaDePagamentoInput formaDePagamantoInput, FormaPagamento formaDePagamento) {
        modelMapper.map(formaDePagamantoInput, formaDePagamento);
    }

}