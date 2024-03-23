package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequestMapping("/restaurantes")
@RestController
public class RestauranteController {

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    RestauranteRepository restauranteRepository;


    @GetMapping
    public List<Restaurante> listar() {

        return cadastratarRestauranteService.todos();
    }

    @GetMapping("/por-taxa-frete")
    public List<Restaurante> restaurantePorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {

        return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
    }

    @GetMapping("/por-nome-e-taxa-frete")
    public List<Restaurante> findTxt(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        return restauranteRepository.findWithCriteria(nome, taxaFreteInicial, taxaFreteFinal);
    }

    @GetMapping("/com-frete-gratis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {

        return restauranteRepository.findComFreteGratis(nome);
    }

    @GetMapping("/por-nome")
    public List<Restaurante> restaurantePorNome(String nome, Long cozinhaId) {

        return restauranteRepository.consultarPorNome(nome, cozinhaId);
    }

    @GetMapping("/primeiro-por-nome")
    public Optional<Restaurante> findFirstRestauranteByName(String nome) {

        return restauranteRepository.readFirstRestauranteByNomeContaining(nome);
    }

    @GetMapping("/primeiro")
    public Optional<Restaurante> buscarPrimeiro() {
        return restauranteRepository.buscarPrimeiro();
    }

    @GetMapping("/top2-por-nome")
    public List<Restaurante> restaurantesTop2PorNome(String nome) {

        return restauranteRepository.streamTop2ByNomeContaining(nome);
    }


    @GetMapping(value = "/{restauranteId}")
    public Restaurante buscar(@PathVariable Long restauranteId) {


        return cadastratarRestauranteService.hasOrNot(restauranteId);

    }

    //Normalmente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    // Usamos o ? para aceitar qualquer tipo de parâmetro...
    public Restaurante adicionar(@RequestBody @Valid  Restaurante restaurante) {

        try {

            return cadastratarRestauranteService.salvar(restaurante);

        } catch (EntidadeNaoEncontradaException e) {
            //Tratamos a exceção do tipo EntidadeNaoEncontradaException e relancamos como uma NegocioException
            throw new NegocioException(e.getMessage());
        }
    }


    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId,
                                 @RequestBody @Valid Restaurante restaurante) {

        Restaurante restauranteAtual = cadastratarRestauranteService.hasOrNot(restauranteId);

        BeanUtils.copyProperties(restaurante, restauranteAtual,
                "id", "fomrasPagamento", "endereco", "dataCadastro", "produtos");
        return cadastratarRestauranteService.salvar(restauranteAtual);

    }

    // A idea é atualizar somente as propriedades que foram especificadas no corpo da requisição...
    @PatchMapping("/{restauranteId}") // Vai atender requis HTTP do tipi PATCH...
    public Restaurante atualizarParcial(@PathVariable Long restauranteId,
                                        @RequestBody Map<String, Object> campos, HttpServletRequest request) {

        //Map<String - propriedade do object | Object - o valor da propriedade>
        Restaurante restauranteAtual = cadastratarRestauranteService.hasOrNot(restauranteId);

        //Este método copia os valores do Map para o objecto restauranteAtual...
        merge(campos, restauranteAtual, request);

        return atualizar(restauranteId, restauranteAtual); // Reutilizamos o método atualiar

    }

    private static void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino,
                              HttpServletRequest request) {
        //Criamos uma instância de ServletServerHttpRequest para passar no construtor de HttpMessageNotReadableException
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

        try {
            // Para converter de Json para Java e vice-versa
            ObjectMapper objectMapper = new ObjectMapper();
            //Criamos uma instância de Restaurante usando os valores do Map...., ou seja, convertemos de json para java
            Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

            dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
                // Vai pegar da classe Restaurante uma propriedade de acordo com o valor de "nomePropriedade"
                Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                field.setAccessible(true); //tornando a variável acessível...

                //Pegamos o valor deste "field" no objecto restauranteOrigem
                Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

                //Atribuimos no campo "field" do objecto "restauranteDestino" o valor que consta em "valorPropriedade"
                ReflectionUtils.setField(field, restauranteDestino, novoValor);
            });

        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }

    }


    @DeleteMapping(value = "/{restauranteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long restauranteId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId

        cadastratarRestauranteService.excluir(restauranteId);
    }


}
