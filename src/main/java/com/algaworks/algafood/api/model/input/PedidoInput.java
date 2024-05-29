package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PedidoInput {

    @NotNull
    @Valid //Para validar recursivamente (as propriedades definidas) o objecto restaurante ...
    private RestauranteIdInput restaurante;

    @NotNull
    @Valid
    private FormaDePagamentoIdInput formaPagamento;

    @NotNull
    @Valid
    private EnderecoInput endereco;

    @NotNull
    @Size(min = 1) //Tme que ter pelo menos um elemento dentro da lista...
    @Valid
    private List<ItemPedidoInput> itens;

}
