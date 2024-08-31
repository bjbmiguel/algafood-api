package com.algaworks.algafood.core.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //seu escopo de ação, aplicada somente em métodos
@Retention(RetentionPolicy.RUNTIME) //vai ser lida em tempo de execução...
@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
public @interface PodeEditarCozinhas {
}
