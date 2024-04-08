package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.FormaDePagamantoInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaDePagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaDePagamanetoInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public FormaDePagamento toDomainObject(FormaDePagamantoInput formaDePagamantoInput) {
        return modelMapper.map(formaDePagamantoInput, FormaDePagamento.class);
    }

    public void copyToDomainObject(FormaDePagamantoInput formaDePagamantoInput, FormaDePagamento formaDePagamento) {
        modelMapper.map(formaDePagamantoInput, formaDePagamento);
    }

}