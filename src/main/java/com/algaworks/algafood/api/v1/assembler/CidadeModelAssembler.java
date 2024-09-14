package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.FactoryLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FactoryLinks links;

    @Autowired
    private AlgaSecurity algaSecurity;

    //Criamos um construtor e chamamos o super passando o controller e model
    public CidadeModelAssembler() {
        super(CidadeController.class, CidadeModel.class);
    }

    @Override
    public CidadeModel toModel(Cidade cidade) {
        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);

        modelMapper.map(cidade, cidadeModel);

        if (algaSecurity.podeConsultarCidades()) {
            cidadeModel.add(links.linkToCidades("cidades"));
        }

        if (algaSecurity.podeConsultarEstados()) {
            cidadeModel.getEstado().add(links.linkToEstado(cidadeModel.getEstado().getId()));
        }

        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(entities);

        if (algaSecurity.podeConsultarCidades()) {
            collectionModel.add(links.linkToCidades());
        }

        return collectionModel;

    }

}