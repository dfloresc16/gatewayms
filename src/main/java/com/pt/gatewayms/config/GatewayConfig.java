package com.pt.gatewayms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	
	@Autowired
	private AuthFilter authFilter;
	
	@Bean
	public RouteLocator configRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/auth/**")
						.uri("lb://msvc-authms"))
				.route(r -> r.path("/user/**")
//						.filters(f -> f.filter(authFilter))
						.uri("lb://userms"))
				.route(r -> r.path("/cv/**")
						.filters(f -> f.filter(authFilter))
						.uri("lb://cv"))
				.route(r -> r.path("/interview/**")
						.filters(f -> f.filter(authFilter))
						.uri("lb://interview"))
				.build();
	}

}
