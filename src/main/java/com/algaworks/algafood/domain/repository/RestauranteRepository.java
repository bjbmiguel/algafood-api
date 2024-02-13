package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository {

    List<Restaurante> listar();
    Restaurante porId(Long restauranteId);
    Restaurante salvar(Restaurante restaurante);
    void remover(Long restauranteId);

}