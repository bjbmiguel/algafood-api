package com.algaworks.algafood.core.modelmapper;

import com.algaworks.algafood.api.v1.model.EnderecoModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.model.input.ItemPedidoInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        var modelMapper = new ModelMapper();

        var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);

        modelMapper.createTypeMap(CidadeInput.class, Cidade.class)
                .addMappings(mapper -> mapper.skip(Cidade::setId));

        //Para n preencher/ignorar o set do pedido durante o mapper , será preenchido de via auto-incremento..
        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class).addMappings(
                mapper -> mapper.skip(ItemPedido::setId));


        enderecoToEnderecoModelTypeMap.<String>addMapping(
                enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),

                (enderecoDest, value) -> enderecoDest.getCidade().setEstado(value)
        );

        return modelMapper;
    }


}