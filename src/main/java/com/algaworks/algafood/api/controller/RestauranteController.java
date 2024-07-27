package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.*;
import com.algaworks.algafood.api.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastratarRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@CrossOrigin //acesso geral para qualquer url, mas só para este endpoint
@RequestMapping("/restaurantes")
@RestController
public class RestauranteController implements RestauranteControllerOpenApi {

    @Autowired
    CadastratarRestauranteService cadastratarRestauranteService;

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    private SmartValidator validator;

    @Autowired
    RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    RestauranteInputAssembler restauranteInputAssembler;

    @Autowired
    RestauranteInputDisassembler restauranteInputDisassembler;

    @Autowired
    private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

    @Autowired
    private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;


    //@JsonView(RestauranteView.Resumo.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteBasicoModel> listar() {
        return restauranteBasicoModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    //Projecao com parâmetro...
    //@JsonView(RestauranteView.ApenasNomes.class)
    @GetMapping(params = "projecao=apenas-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public  CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return restauranteApenasNomeModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }


    /*
    //@RequestParam(required = false) --> o parâmetro "projecao" n é obrigatório
    @GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {

        List<Restaurante> restauranteList = restauranteRepository.findAll();
        List<RestauranteModel> restauranteModels = restauranteModelAssembler.toCollectionModel(restauranteList);

        //Embrulhamos um "restauranteModels" dentro do MappingJacksonValue
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(restauranteModels);

        // Definimos a view a usar neste MappingJacksonValue

        mappingJacksonValue.setSerializationView(RestauranteView.Resumo.class);

        if("apenas-nome".equalsIgnoreCase(projecao)){

            mappingJacksonValue.setSerializationView(RestauranteView.ApenasNomes.class);

        }else if("completo".equalsIgnoreCase(projecao)){

            mappingJacksonValue.setSerializationView(null);
        }

        return mappingJacksonValue;
    }

*/

    /*
    //Projecao sem parâmetro...
    @GetMapping
    public List<RestauranteModel> listar() {
        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    //Projecao com parâmetro...
    @JsonView(RestauranteView.Resumo.class)
    @GetMapping(params = "projecao=resumo")
    public List<RestauranteModel> listarResumido() {
        return listar();
    }

    @JsonView(RestauranteView.ApenasNomes.class)
    @GetMapping(params = "projecao=apenas-nomes")
    public List<RestauranteModel> listarApenasNomes() {
        return listar();
    }
*/
    @GetMapping(path = "/por-taxa-frete", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteModel> restaurantePorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {

        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal));
    }

    @GetMapping(path = "/por-nome-e-taxa-frete", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteModel> findTxt(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findWithCriteria(nome, taxaFreteInicial, taxaFreteFinal));
    }

    @GetMapping(path = "/com-frete-gratis", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteModel> restaurantesComFreteGratis(String nome) {

        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findComFreteGratis(nome));
    }

    @GetMapping(path = "/por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteModel> restaurantePorNome(String nome, Long cozinhaId) {

        return restauranteModelAssembler.toCollectionModel(restauranteRepository.consultarPorNome(nome, cozinhaId));
    }

    @GetMapping(path = "/primeiro-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Restaurante> findFirstRestauranteByName(String nome) {

        return restauranteRepository.readFirstRestauranteByNomeContaining(nome);
    }

    @GetMapping(path = "/primeiro" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<RestauranteModel> buscarPrimeiro() {
        return Optional.of(restauranteModelAssembler.toModel(restauranteRepository.buscarPrimeiro().get()));
    }

    @GetMapping(path = "/top2-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<RestauranteModel> restaurantesTop2PorNome(String nome) {
        return restauranteModelAssembler.toCollectionModel(restauranteRepository.streamTop2ByNomeContaining(nome));
    }


    @GetMapping(value = "/{restauranteId}")
    public RestauranteModel buscar(@PathVariable Long restauranteId) {

        return restauranteModelAssembler.toModel(cadastratarRestauranteService.findById(restauranteId));

    }

    //Normalmente
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED) // 201
    // Usamos o ? para aceitar qualquer tipo de parâmetro...
    public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {

        try {

            Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

            return restauranteModelAssembler.toModel(cadastratarRestauranteService.salvar(restaurante));

        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException  e) {
            //Tratamos a exceção do tipo EntidadeNaoEncontradaException e relancamos como uma NegocioException
            throw new NegocioException(e.getMessage());
        }
    }


    @PutMapping(path = "/{restauranteId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public RestauranteModel atualizar(@PathVariable Long restauranteId,
                                 @RequestBody @Valid RestauranteInput restauranteInput) {

        try {


            Restaurante restauranteAtual = cadastratarRestauranteService.findById(restauranteId);

            //copyProperties from InputModel to Entity
            restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
            //BeanUtils.copyProperties(restaurante, restauranteAtual,
              //      "id", "formasPagamento", "endereco", "dataCadastro", "produtos");

            return restauranteModelAssembler.toModel(cadastratarRestauranteService.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException  e) {
            throw new NegocioException(e.getMessage());
        }

    }

    // A idea é atualizar somente as propriedades que foram especificadas no corpo da requisição...
    @PatchMapping(path = "/{restauranteId}", produces = MediaType.APPLICATION_JSON_VALUE) // Vai atender requis HTTP do tipi PATCH...
    public RestauranteModel atualizarParcial(@PathVariable Long restauranteId,
                                        @RequestBody Map<String, Object> campos, HttpServletRequest request) {

        //Map<String - propriedade do object | Object - o valor da propriedade>
        Restaurante restauranteAtual = cadastratarRestauranteService.findById(restauranteId);

        //Este método copia os valores do Map para o objecto restauranteAtual...
        merge(campos, restauranteAtual, request);

        //Validamos o objecto
        validate(restauranteAtual, "restaurante");

        return atualizar(restauranteId, restauranteInputAssembler.fromDomainModelToInputModel(restauranteAtual)); // Reutilizamos o método atualiar

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

    private void validate(Restaurante restaurante, String objectName) {

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);

        validator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidacaoException(bindingResult);
        }
    }


    @DeleteMapping(value = "/{restauranteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long restauranteId) {
        //@PathVariable vai extrair os valores da url e fazer o bind  de forma automática para o parâmetro cozinhaId

        cadastratarRestauranteService.excluir(restauranteId);
    }

    @PutMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  ativar(@PathVariable Long restauranteId) {
        cadastratarRestauranteService.ativar(restauranteId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{restauranteId}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  inativar(@PathVariable Long restauranteId) {
        cadastratarRestauranteService.inativar(restauranteId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{restauranteId}/fechamento") //Modelando uma regra de negócio como um recurso
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
        cadastratarRestauranteService.fechar(restauranteId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{restauranteId}/abrir") //Modelando uma regra de negócio como um recurso
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
        cadastratarRestauranteService.abrir(restauranteId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastratarRestauranteService.ativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastratarRestauranteService.inativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }


}
