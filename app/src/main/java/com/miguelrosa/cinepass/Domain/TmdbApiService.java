package com.miguelrosa.cinepass.Domain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<UpComingMoviesResponse> getUpComingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey
    );
}
