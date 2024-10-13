package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
                .oauth2ResourceServer().jwt();//Indica que os tokens usados para proteger os recursos são do tipo jwt...


        return http.formLogin(Customizer.withDefaults()).build();
    }
}
