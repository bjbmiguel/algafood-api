package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig { //Valida juntos do Authorization Server se determinado token é validdo..

    //Este bean configura este servidor como um Resource Server no contexto do OAuth2, que utiliza tokens opacos para autenticação.
   //A idea é o Resource server ler os token opacos
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**").authenticated()
                .and()
                .csrf().disable() //Desativa a proteção contra CSRF (Cross-Site Request Forgery). Normalmente, essa proteção é desativada em APIs REST, pois as requisições não utilizam cookies/sessões como em aplicações tradicionais de páginas web.
                .cors().and() //Habilita o CORS (Cross-Origin Resource Sharing). Isso permite que a aplicação responda a requisições de origens diferentes (diferentes domínios), o que é comum em APIs expostas publicamente.
                //.oauth2ResourceServer().opaqueToken();//Indica que os tokens usados para proteger os recursos são tokens opacos...
                .oauth2ResourceServer().jwt()//Indica que os tokens usados para proteger os recursos são do tipo jwt...
                .jwtAuthenticationConverter(jwtAuthenticationConverter());//Passamos um conversor


        return http.formLogin(customizer -> customizer.loginPage("/login")).build();
    }

    //O método jwtAuthenticationConverter() é utilizado para converter um token JWT em uma lista de autoridades (ou permissões),
    // que são usadas pelo Spring Security para controlar o acesso aos recursos da aplicação
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            //Ler permissões vindas dos scopes
            Collection<GrantedAuthority> grantedAuthorities  = authoritiesConverter.convert(jwt);

            //"authorities" é um campo costumizado no AuthorizationServerConfig
            List<String> authorities = jwt.getClaimAsStringList("authorities");

            if (authorities == null) {
                return grantedAuthorities; //Retorna lista com os scopes, para o fluxo de client credentials
            }

            grantedAuthorities.addAll(authorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));

            return grantedAuthorities;
        });

        return converter;
    }
}
