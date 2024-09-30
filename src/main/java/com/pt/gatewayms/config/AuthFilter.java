package com.pt.gatewayms.config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.pt.gatewayms.models.TokenDTO;

import jakarta.ws.rs.Encoded;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter {
	
	
	private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
	
	@Value("${config.url}")
	private String tokenUrl;

    @Autowired
    private WebClient.Builder webClient;

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	log.info(String.format("$config.url:", null));
        if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            return onError(exchange,HttpStatus.BAD_REQUEST);
        }
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String[] chunks = token.split(" ");
        if(chunks.length != 2 || !chunks[0].equals("Bearer")){
            return onError(exchange,HttpStatus.BAD_REQUEST);
        }
        return webClient.build().post().uri(tokenUrl/*"http://host.docker.internal:8082/auth/validate?token="*/+chunks[1]).retrieve()
                .bodyToMono(TokenDTO.class).map(t->{
                    return exchange;
                }).flatMap(chain::filter);
    }
}
