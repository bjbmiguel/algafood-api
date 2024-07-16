package com.algaworks.algafood.api.openapi.controller;

import com.algaworks.algafood.api.exceptionhanlder.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

//Aqui nesta interface só temos código de documentação da API
@Api(tags = "Cidades") //Tag a ser usada na documentação da API / SpringFox & OpenAPI/Swagger
public interface CidadeControllerOpenApi {

    @ApiOperation("Lista as cidades")
     List<CidadeModel> listar();

    @ApiOperation("Busca uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
     CidadeModel buscar(@ApiParam(value = "ID de uma cidade", example = "1") Long cidadeId);

    @ApiResponses({
            @ApiResponse(code = 201, message = "Cidade cadastrada"),
    })
    @ApiOperation("Cadastra uma cidade")
     CidadeModel adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade", required = true) CidadeInput cidadeInput) ;

    @ApiResponses({
            @ApiResponse(code = 200, message = "Cidade atualizada"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    @ApiOperation("Atualiza uma cidade por ID")
     CidadeModel atualizar(@ApiParam(value = "ID de uma cidade", example = "1", required = true) Long cidadeId,
                                CidadeInput cidadeInput);

    @ApiResponses({
            @ApiResponse(code = 204, message = "Cidade excluída"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    @ApiOperation("Exclui uma cidade por ID")
     void remover(@ApiParam(value = "ID de uma cidade", example = "1", required = true) Long cidadeId) ;
}
