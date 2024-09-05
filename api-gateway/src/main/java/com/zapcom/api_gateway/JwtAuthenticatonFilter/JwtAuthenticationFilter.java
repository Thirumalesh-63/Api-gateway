package com.zapcom.api_gateway.JwtAuthenticatonFilter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	@Autowired
	private RestTemplate template;

	public JwtAuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}
				String jwtToken = null;
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					jwtToken = authHeader;
					authHeader = authHeader.substring(7);
				}

				// REST call to AUTH service to validate token and get user details
				ResponseEntity<Map> response = template
						.getForEntity("http://localhost:8083/auth/validate?token=" + authHeader, Map.class);
				if (response.getStatusCode() != HttpStatus.OK) {
					throw new RuntimeException("Unauthorized access");
				}

				Map<String, Object> responseBody = response.getBody();
				if (responseBody == null) {
					throw new RuntimeException("Unauthorized access");
				}

				// Optionally: Set user details or roles in request headers
				String username = (String) responseBody.get("username");
				String roles = responseBody.get("roles").toString();

				// Create a new request with updated headers
				ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("username", username)
						.header("roles", roles).header("Authorization", jwtToken).build();

				// Create a new exchange with the modified request
				return chain.filter(exchange.mutate().request(modifiedRequest).build());

			}
			return chain.filter(exchange);
		});
	}

	public static class Config {

	}
}