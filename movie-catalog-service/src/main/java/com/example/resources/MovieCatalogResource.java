package com.example.resources;

import java.lang.reflect.ParameterizedType;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.example.models.CatalogItem;
import com.example.models.Movie;
import com.example.models.Rating;
import com.example.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping(value="/{userId}")
	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId)
	{
		//get  all rated movie Id's
		//for ecah movie id, call info service to get movie details
		//Put all together
	
		//restTemplate.getForObject("http://localhost:9001/movies/m123", Movie.class);
		
		
		/*
		 * List<Rating> ratings= Arrays.asList( new Rating("movieId1",1), new
		 * Rating("movieId2",4), new Rating("movieId3",3) );
		 */
		UserRating ratings= restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId,UserRating.class);
		/*
		 * return ratings.stream().map(rating -> { Movie
		 * movie=restTemplate.getForObject("http://localhost:9001/movies/"+rating.
		 * getMovieId(), Movie.class); return new CatalogItem(movie.getName(),
		 * "desc1",rating.getRating()) }) .collect(Collectors.toList());
		 */
		List<CatalogItem> catalogItems= new  ArrayList<>();
		for(Rating rating :ratings.getUserRating())
		{
			
			//calling another service using REst template
			Movie movie= restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
			/*
			//calling another service using web client
			Movie movie1= webClientBuilder.build() // webclientbuilder using builder patter and giving us webclient
							.get()  //can be http method get,post, put etc
							.uri("http://localhost:9001/movies/"+rating.getMovieId())
							.retrieve()
							.bodyToMono(Movie.class) //whatever resp bost it get converts to instance of movie class
							.block();
			*/
			                
							
			// Here I am making calll to movie info service to get movie info for movie id
			catalogItems.add(new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating()));
		}
		return catalogItems;
		
	//	return Collections.singletonList(new CatalogItem("movie1", "desc1", 4));
		
	}
	
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId)
	{
		return Arrays.asList(new CatalogItem("No Movie", "", 0));
	}
	
	
}
 