package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.FormaDePagamentoModel;
import com.algaworks.algafood.domain.model.FormaDePagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FormaDePagamentoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public FormaDePagamentoModel toModel(FormaDePagamento formaDePagamaneto) {
        return modelMapper.map(formaDePagamaneto, FormaDePagamentoModel.class);
    }

    public List<FormaDePagamentoModel> toCollectionModel(List<FormaDePagamento> formaDePagamentos) {
        return formaDePagamentos.stream()
                .map(formaDePagamento -> toModel(formaDePagamento))
                .collect(Collectors.toList());
    }

}