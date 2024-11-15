package com.pt.gatewayms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import com.pt.gatewayms.models.CommonResponse;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RefreshScope
@Component
public class AuthFilter implements GatewayFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${config.url}")
    private String tokenUrl;

    @Autowired
    private WebClient.Builder webClient;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Token URL: {}", tokenUrl);

        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.error("Authorization header is missing");
            return onError(exchange, HttpStatus.BAD_REQUEST, "Authorization header is missing");
        }

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String[] chunks = token.split(" ");
        if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
            log.error("Invalid Bearer token format");
            return onError(exchange, HttpStatus.BAD_REQUEST, "Invalid Bearer token format");
        }

        log.info("Calling auth service with token: {}", chunks[1]);

        return webClient.build()
            .post()
            .uri(tokenUrl + chunks[1])
            .retrieve()
            .bodyToMono(CommonResponse.class)
            .flatMap(response -> {
                log.info("Received response from auth service: {}", response);
                return handleAuthResponse(response, exchange, chain);
            })
            .onErrorResume(error -> {
                if (error instanceof WebClientResponseException webClientException) {
                    HttpStatus status = (HttpStatus) webClientException.getStatusCode();
                    log.error("Auth service returned status: {}", status);
                    return onError(exchange, status, webClientException.getResponseBodyAsString());
                } else {
                    log.error("Error calling auth service: {}", error.getMessage());
                    return onError(exchange, HttpStatus.SERVICE_UNAVAILABLE, error.getMessage());
                }
            });
    }

    // Método auxiliar para manejar la respuesta del servicio de autenticación
    private Mono<Void> handleAuthResponse(CommonResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Handling auth response. Success: {}", response.isSuccess());

        if (response.isSuccess()) {
            log.info("Token validated successfully. Proceeding with the request.");
            return chain.filter(exchange);
        } else {
            log.error("Auth service returned an error: {}", response.getErrorMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED, response.getErrorMessage());
        }
    }

    // Método para retornar respuestas de error
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String errorResponse = String.format(
            "{ \"success\": false, \"httpstatus\": %d, \"errorMessage\": \"%s\", \"message\": \"service execute\" }",
            status.value(), status.getReasonPhrase().toUpperCase()
        );

        DataBufferFactory bufferFactory = response.bufferFactory();
        DataBuffer buffer = bufferFactory.wrap(errorResponse.getBytes(StandardCharsets.UTF_8));

        log.info("Returning error response: {}", errorResponse);
        return response.writeWith(Mono.just(buffer));
    }

}
