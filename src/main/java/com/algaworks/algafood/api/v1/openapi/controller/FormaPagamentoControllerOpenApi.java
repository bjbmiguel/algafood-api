package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaDePagamentoInput;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Formas de Pagamento")
public interface FormaPagamentoControllerOpenApi {

    ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request);

    ResponseEntity<FormaPagamentoModel> buscar(Long formaPagamentoId, ServletWebRequest request);

    FormaPagamentoModel adicionar(FormaDePagamentoInput formaPagamentoInput);

    FormaPagamentoModel atualizar(Long formaPagamentoId,FormaDePagamentoInput formaPagamentoInput);

    ResponseEntity<Void> remover(Long formaPagamentoId);

}