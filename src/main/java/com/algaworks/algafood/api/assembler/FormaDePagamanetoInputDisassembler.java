package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.FormaDePagamantoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaDePagamanetoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toDomainObject(FormaDePagamantoInput formaDePagamantoInput) {
        return modelMapper.map(formaDePagamantoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaDePagamantoInput formaDePagamantoInput, FormaPagamento formaDePagamento) {
        modelMapper.map(formaDePagamantoInput, formaDePagamento);
    }

}