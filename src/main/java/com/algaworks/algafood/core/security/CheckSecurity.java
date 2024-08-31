package com.algaworks.algafood.core.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    public  @interface  Cozinhas{

        @Target(ElementType.METHOD) //seu escopo de ação, aplicada somente em métodos
        @Retention(RetentionPolicy.RUNTIME) //vai ser lida em tempo de execução...
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        public @interface PodeConsultar { }

        @Target(ElementType.METHOD) //seu escopo de ação, aplicada somente em métodos
        @Retention(RetentionPolicy.RUNTIME) //vai ser lida em tempo de execução...
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
        public @interface PodeEditar { }

    }


    public @interface Restaurantes{

        @Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and  hasAuthority('EDITAR_RESTAURANTES')")
        public @interface PodeEditar { }

        @Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        public @interface PodeConsultar { }

    }
}
