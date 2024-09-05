package com.zapcom.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("http://localhost:4200");
            config.addAllowedOrigin("http://localhost:4200/adminhomepage/cruiseline");
            config.addAllowedMethod("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);  // Optional if credentials are needed
            return config;
        })).csrf(csrf -> csrf.disable());
		return http.build();
	}
	
	

}
