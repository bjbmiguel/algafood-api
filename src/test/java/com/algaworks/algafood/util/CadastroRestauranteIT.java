package com.algaworks.algafood.util;


import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private String jsonRestauranteCorreto;
    private String jsonRestauranteSemFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComoCozinhaInexistente;

    private Cozinha cozinha1, cozinha2;
    private Restaurante burgerTopRestaurante;

    private static  final Long RESTAURANTE_ID_INEXISTENTE = 100L;
    private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
    private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach  // Este método é executado antes de cada teste...
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/restaurantes";
        databaseCleaner.clearTables();// Faz um reset do Banco de Dados antes de cada teste...
        prepararDados(); //Adiciona dados de teste...
        jsonRestauranteCorreto = ResourceUtils.getContentFromResource("/json/restaurante-correto.json");
        jsonRestauranteSemFrete = ResourceUtils.getContentFromResource(
                "/json/restaurante-sem-frete.json");

        jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource(
                "/json/restaurante-sem-cozinha.json");

        jsonRestauranteComoCozinhaInexistente = ResourceUtils.getContentFromResource(
                "/json/restaurante-com-cozinha-inexistente.json");
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
        given()
                .body(jsonRestauranteCorreto)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
        given()
                .body(jsonRestauranteSemFrete)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
        given()
                .body(jsonRestauranteSemCozinha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
        given()
                .body(jsonRestauranteComoCozinhaInexistente)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
        given()
                .pathParam("restauranteId", burgerTopRestaurante.getId())
                .accept(ContentType.JSON)
                .when()
                .get("/{restauranteId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(burgerTopRestaurante.getNome()));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
        given()
                .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
                .accept(ContentType.JSON)
                .when()
                .get("/{restauranteId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    @Test
    public void deveRetornarRespostaEStatus404_QuandoConsultarRestauranteInexistente() {
        given()
                .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE) //Passamos o valor do parâmetro...
                .accept(ContentType.JSON)
                .when()
                .get("/{restauranteId}")//Complementamos o endpoint adicionando o parâmetro
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    private void prepararDados() {

        cozinha1 = new Cozinha();
        cozinha1.setNome("Brasileira");
        cozinhaRepository.save(cozinha1);

        cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        cozinhaRepository.save(cozinha2);

        burgerTopRestaurante = new Restaurante();
        burgerTopRestaurante.setNome("Burger Top");
        burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
        burgerTopRestaurante.setCozinha(cozinha2);
        restauranteRepository.save(burgerTopRestaurante);

        Restaurante comidaMineiraRestaurante = new Restaurante();
        comidaMineiraRestaurante.setNome("Comida Mineira");
        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
        comidaMineiraRestaurante.setCozinha(cozinha1);
        restauranteRepository.save(comidaMineiraRestaurante);


    }
}
