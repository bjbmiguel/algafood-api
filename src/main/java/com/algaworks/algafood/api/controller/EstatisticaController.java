package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import org.hibernate.type.OffsetTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticaController {

    @Autowired
    private VendaQueryService vendaQueryService;

    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(@RequestParam(required = false, defaultValue = "+00:00") String timeOffset,
                                                    VendaDiariaFilter filtro) {

        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }


}
