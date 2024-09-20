package com.bananaapps.MyOnlineShoppingService.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServiceConfig {

//    @Value("${issuer-uri}")
//    private String issuerUri;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/accounts/").hasAuthority("SCOPE_accounts.read")
                .mvcMatchers("/accounts/**").authenticated()
                .mvcMatchers(HttpMethod.GET,"/customers/**").hasAuthority("SCOPE_accounts.read")
                .mvcMatchers(HttpMethod.POST,"/customers/**").hasAuthority("SCOPE_accounts.write")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "http://localhost:9000/oauth2/jwks";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

}