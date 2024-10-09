package com.algaworks.algafood.api.v1.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

//@ApiModel(value = "Cidade", description = "Representa uma cidade")
@Relation(collectionRelation = "cidades") //Alteramos o nome da coleção no payload da resposta
@Setter
@Getter
public class CidadeModel extends RepresentationModel<CidadeModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Uberlândia")
    private String nome;
    private EstadoModel estado;

}