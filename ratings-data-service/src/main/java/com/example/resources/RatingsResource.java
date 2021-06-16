package com.example.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.models.Rating;
import com.example.models.UserRating;


@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

	@RequestMapping("/{movieId}")
	public Rating getMovieInfo(@PathVariable("movieId") String movieId)
	{
		return new Rating(movieId,5);
		//return new Movie(movieId, "TestMovie");
	}
	@RequestMapping("users/{userId}")
	public UserRating getUserRatings(@PathVariable("userId") String userId)
	{
		List<Rating> ratings= Arrays.asList(new Rating("550",1),new Rating("551",2),new Rating("552",3));
		UserRating userRating = new UserRating();
		userRating.setUserRating(ratings);
				return userRating;
		//return new Movie(movieId, "TestMovie");
	}
}
