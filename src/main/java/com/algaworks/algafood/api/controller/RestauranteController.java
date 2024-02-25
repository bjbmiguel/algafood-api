package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.algaworks.algafood.infrastructure.spec.RestauranteSpecs.*;

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
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {


        Optional<Restaurante> restaurante = cadastratarRestauranteService.buscar(restauranteId);

        if (restaurante.isPresent()) {
            return ResponseEntity.ok().body(restaurante.get());
        }

        return ResponseEntity.notFound().build();
    }

    //Normalmente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    // Usamos o ? para aceitar qualquer tipo de parâmetro...
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {

        try {
            restaurante = cadastratarRestauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);

        } catch (EntidadeNaoEncontradaException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
                                       @RequestBody Restaurante restaurante) {
        try {

            Optional<Restaurante> restauranteAtual = cadastratarRestauranteService.buscar(restauranteId);

            if (restauranteAtual.isPresent()) {
                BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
                Restaurante restauranteSalvo = cadastratarRestauranteService.salvar(restauranteAtual.get());
                return ResponseEntity.ok(restauranteSalvo);
            }
            return ResponseEntity.notFound().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // A idea é atualizar somente as propriedades que foram especificadas no corpo da requisição...
    @PatchMapping("/{restauranteId}") // Vai atender requis HTTP do tipi PATCH...
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) {

        //Map<String - propriedade do object | Object - o valor da propriedade>

        Optional<Restaurante> restauranteAtual = cadastratarRestauranteService.buscar(restauranteId);

        if (restauranteAtual.isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        //Este método copia os valores do Map para o objecto restauranteAtual...
        merge(campos, restauranteAtual.get());

        return atualizar(restauranteId, restauranteAtual.get()); // Reutilizamos o método atualiar

    }

    private static void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
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

            System.out.println(nomePropriedade + " = " + valorPropriedade);

            //Atribuimos no campo "field" do objecto "restauranteDestino" o valor que consta em "valorPropriedade"
            ReflectionUtils.setField(field, restauranteDestino, novoValor);

        });
    }


    @DeleteMapping(value = "/{restauranteId}")
    public ResponseEntity<?> remover(@PathVariable Long restauranteId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId
        try {

            cadastratarRestauranteService.excluir(restauranteId);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //A entidade não foi encontrada...
        } catch (EntidadeEmUsoException e) {
            // Se a chave  estrangeira recurso numa outra tabela então essa exceção será capturada aqui como "conflito"
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
