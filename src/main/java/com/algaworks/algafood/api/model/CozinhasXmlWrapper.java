package com.algaworks.algafood.api.model;

import com.algaworks.algafood.domain.model.Cozinha;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@JsonRootName("Cozinhas") // Para alterar o nome do elemento root do recurso...
public class CozinhasXmlWrapper {

    @JsonProperty("cozinha")
    @JacksonXmlElementWrapper(useWrapping = false) // Para desabilitar o embrulho, ou seja, a segunda tag <cozinhas>
    @NonNull // Vai gerar um construtor com cozinhas...
    private List<Cozinha> cozinhas;
}
