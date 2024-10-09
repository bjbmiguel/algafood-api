package com.algaworks.algafood.core.springdoc;

import com.algaworks.algafood.api.exceptionhanlder.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@SecurityScheme(name = "security_auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                scopes = {
                        @OAuthScope(name = "READ", description = "read scope"),
                        @OAuthScope(name = "WRITE", description = "write scope")
                }
        )))
public class SpringDocConfig {

    private static final String badRequestResponse = "BadRequestResponse";
    private static final String notFoundResponse = "NotFoundResponse";
    private static final String notAcceptableResponse = "NotAcceptableResponse";
    private static final String internalServerErrorResponse = "InternalServerErrorResponse";

    //Método responsável por configurar a documentação principal da API.
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AlgaFood API")
                        .version("v1")
                        .description("REST API do AlgaFood")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.com")
                        )
                ).externalDocs(new ExternalDocumentation()
                        .description("AlgaWorks")
                        .url("https://algaworks.com")
                ).tags(Arrays.asList(
                        new Tag().name("Cidades").description("Gerencia as cidades"),
                        new Tag().name("Cozinhas").description("Gerencia as cozinhas"),
                        new Tag().name("Pedidos").description("Gerencia os pedidos"),
                        new Tag().name("Restaurantes").description("Gerencia os restaurantes"),
                        new Tag().name("Estados").description("Gerencia os estados"),
                        new Tag().name("Usuários").description("Gerencia os usuários"),
                        new Tag().name("Permissões").description("Gerencia as permissões"),
                        new Tag().name("Estatísticas").description("Estatísticas da AlgaFood"),
                        new Tag().name("Produtos").description("Gerencia os produtos de restaurantes"),
                        new Tag().name("Formas de Pagamento").description("Gerencia as formas de pagamento"),
                        new Tag().name("Grupos").description("Gerencia os grupos de usuários")
                )).components(new Components()
                        .schemas(gerarSchemas())
                        .responses(gerarResponses())
                );
    }

    /*
    @Bean
    public OpenApiCustomiser openApiCustomiser(){
        return openApi -> {
          openApi.getPaths().values().stream()
                  .flatMap(pathItem -> pathItem.readOperations().stream())
                  .forEach(operation -> {

                      //Pegamos todas as respostas Ex. 200, 201...
                      ApiResponses responses = operation.getResponses();
                      //Adicionando novas res
                      ApiResponse apiResponseNaoEncontrado = new ApiResponse().description("Recurso não encontrado");
                      ApiResponse apiResponseErroInterno = new ApiResponse().description("Erro interno no servidor");
                      ApiResponse apiResponseSemRepresentacao = new ApiResponse()
                              .description("Recurso não possui uma representação que poderia ser aceita pelo consumidor");
                      responses.addApiResponse("404", apiResponseNaoEncontrado);
                      responses.addApiResponse("406", apiResponseSemRepresentacao);
                      responses.addApiResponse("500", apiResponseErroInterno);

                  });
        };
    }

     */

    //Este método personaliza globalmente as respostas HTTP que serão documentadas para cada operação.
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                ApiResponses responses = operation.getResponses();
                                switch (httpMethod) {
                                    case GET:
                                        responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case POST:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case PUT:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case DELETE:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                }
                            })
                    );
        };
    }

    //Como a class Problem não é um controller então precisamos adiciona-la manualmente
    private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        //Criamos os schemas
        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
        Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

        schemaMap.putAll(problemSchema);
        schemaMap.putAll(problemObjectSchema);

        return schemaMap;
    }

    //Criamos uma mapa de resposta global,
    // Este método cria um conjunto de respostas de erro padrão para ser utilizado na documentação da API
    private Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        //Definimos o tipo de media type (json) a ser usado, e
        // Referenciamos um esquema  (uma estrutura de dados) chamado "Problema",
        // que representa o modelo que descreve um problema genérico
        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<Problem>().$ref("Problema")));

        //Adiciona diferentes respostas de erro ao mapa:
        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .description("Requisição inválida")
                .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Recurso não encontrado")
                .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
                .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .description("Erro interno no servidor")
                .content(content));

        return apiResponseMap;
    }
}
