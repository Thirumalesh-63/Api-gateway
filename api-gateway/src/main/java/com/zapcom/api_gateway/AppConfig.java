package com.zapcom.api_gateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {

	   @Bean
	    public RestTemplate template(){
	       return new RestTemplate();
	    }

	   @Bean
	   public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
	       http
	           .csrf(csrf -> csrf.disable());
	       return http.build();
	   }

//	    @Bean
//	    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//	        return http
//	            .csrf(csrf -> csrf.csrfTokenRepository(
//	                CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
//	            .build();
//	    }

//	    @Bean
//	    WebFilter csrfWebFilter() {
//	        return (exchange, chain) -> {
//	            exchange.getResponse().beforeCommit(() -> Mono.defer(() -> {
//	                Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
//	                return csrfToken != null ? csrfToken.then() : Mono.empty();
//	            }));
//	            return chain.filter(exchange);
//	        };
//	    }
}
