package com.example;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableCircuitBreaker
public class MovieCatalogServiceApplication {
	
	@Bean
	@LoadBalanced //This looks for service discovery(Eureka here) and does load balancing
	public RestTemplate getRestTemplate() { //resttemplate bean creation, is like producer that gives resttemplate when ever it called by @Autowire restTemplate
	/*	HttpComponentsClientHttpRequestFactory clientHttpRequestFactory= new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(clientHttpRequestFactory);*/
		return new RestTemplate();
	}

	
	@Bean
	public WebClient.Builder getWebClient(){
		return WebClient.builder();
	}
	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
