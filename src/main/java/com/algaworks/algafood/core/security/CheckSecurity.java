package com.algaworks.algafood.core.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

    public  @interface  Cozinhas{

        @Target(METHOD) //seu escopo de ação, aplicada somente em métodos
        @Retention(RUNTIME) //vai ser lida em tempo de execução...
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        public @interface PodeConsultar { }

        @Target(METHOD) //seu escopo de ação, aplicada somente em métodos
        @Retention(RUNTIME) //vai ser lida em tempo de execução...
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
        public @interface PodeEditar { }

    }


    public @interface Restaurantes{

        @Target(METHOD)
        @Retention(RUNTIME)
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and  hasAuthority('EDITAR_RESTAURANTES')")
        public @interface PodeGerenciarCadastro { }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and "
                + "(hasAuthority('EDITAR_RESTAURANTES') or "
                + "@algaSecurity.gerenciaRestaurante(#restauranteId))")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeGerenciarFuncionamento { }

        @Target(METHOD)
        @Retention(RUNTIME)
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        public @interface PodeConsultar { }

    }

    public @interface Pedidos {


        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
                + "@algaSecurity.getUsuarioId() == returnObject.cliente.id or "
                + "@algaSecurity.gerenciaRestaurante(returnObject.restaurante.id)")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeBuscar { }

        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
                + "@algaSecurity.getUsuarioId() == #pedidoFilter.clienteId  or "
                + "@algaSecurity.gerenciaRestaurante(#pedidoFilter.restauranteId)")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodePesquisar { }



        //Aqui usamos o PreAuthorize porque temos as condições necessárias para verifciar antes se o user tem permissão ou não para execu a opera...
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('GERENCIAR_PEDIDOS') or "
                + "@algaSecurity.verificaRespDoRestaurantePorPedido(#codigoPedido))")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeGerenciarPedidos { }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface PodeCriar { }

    }
}
