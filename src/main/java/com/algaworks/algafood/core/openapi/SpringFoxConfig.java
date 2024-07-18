package com.algaworks.algafood.core.openapi;


import com.algaworks.algafood.api.exceptionhanlder.Problem;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PedidosResumoModelOpenApi;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class SpringFoxConfig {

    TypeResolver typeResolver = new TypeResolver();

    @Bean
    public Docket apiDocket() { //Configuração de OpenAPI/Swagger com SpringFox:
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
                .paths(PathSelectors.any())
//          .paths(PathSelectors.ant("/restaurantes/*"))
                .build().
                useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, globalGetResponseMessages())
                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
                .globalResponses(HttpMethod.POST, globalPostResponseMessages())
                .globalResponses(HttpMethod.PUT, globalPutResponseMessages())
//                .globalRequestParameters(Collections.singletonList( // Config de forma global de parâmetros de pes
//                                new RequestParameterBuilder()
//                                        .name("campos")
//                                        .description("Nomes das propriedades para filtrar na resposta, separados por vírgula")
//                                        .in(ParameterType.QUERY)
//                                        .required(true)
//                                        .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
//                                        .build()))
                .additionalModels(typeResolver.resolve(Problem.class))
                .ignoredParameterTypes(ServletWebRequest.class,
                        URL.class, URI.class, URLStreamHandler.class, Resource.class,
                        File.class, InputStream.class)
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, CozinhaModel.class),
                        CozinhasModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, PedidoResumoModel.class),
                        PedidosResumoModelOpenApi.class))
                .ignoredParameterTypes(ServletWebRequest.class)
                .apiInfo(apiInfo())
                .tags(new Tag("Cidades", "Gerencia as cidades"),
                        new Tag("Cozinhas", "Gerencia as cozinhas"),
                        new Tag("Pedidos", "Gerencia os pedidos"),
                        new Tag("Restaurantes", "Gerencia os restaurantes"),
                        new Tag("Estados", "Gerencia os estados"),
                        new Tag("Produtos", "Gerencia os produtos de restaurantes"),
                        new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
                        new Tag("Grupos", "Gerencia os grupos de usuários"));//Criamos uma tag
    }

    //Todos os endpoint com o verbo GET podem retornar os códigos abaixo...
    private List<Response> globalGetResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())//Vinculamos o modelo de representação do problema com o código de status (500)
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que pode ser aceita pelo consumidor.")
                        .build()
        );
    }

    //Todos os endpoint com o verbo DELETE podem retornar os códigos de status abaixo...
    private List<Response> globalDeleteResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.CONFLICT.value()))
                        .description("Recurso em uso.")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .description("Recurso inexistente")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build()
        );
    }
    //Todos os endpoint com o verbo POST podem retornar os códigos de status abaixo...
    private List<Response> globalPostResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Os dados fornecidos como entradas são inválidos, Corrija e tente novamente")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder() //N faz sentido vincular o modelo de representação do problema
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))//Refere-se ao corpo da resposta.
                        .description("Recurso não possui representação que pode ser aceita pelo consumidor. Verifique o cabeçalho 'Accept' da requisição.")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())) //refere-se ao corpo da requisição..
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .description("Recurso inexistente")
                        .build()
        );
    }

    //Todos os endpoint com o verbo PUT podem retornar os códigos de status abaixo...
    private List<Response> globalPutResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Os dados fornecidos como entradas são inválidos, Corrija e tente novamente")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que pode ser aceita pelo consumidor. Verifique o cabeçalho 'Accept' da requisição.")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .description("Recurso inexistente")
                        .build()
        );
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AlgaFood API")
                .description("API aberta para clientes e restaurantes")
                .version("1")
                .contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
                .build();
    }

    //Criamos a referência da representação do Problem...
    private Consumer<RepresentationBuilder> getProblemaModelReference() {
        return r -> r.model(m -> m.name("Problema")
                .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
                        q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhanlder")))));
    }

    @Bean
    public JacksonModuleRegistrar springFoxJacksonConfig() {
        return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
    }

}
