package com.miguelrosa.cinepass.Domain;

import com.miguelrosa.cinepass.Domain.Models.CreateSessionBody;
import com.miguelrosa.cinepass.Domain.Models.FavoriteRequest;
import com.miguelrosa.cinepass.Domain.Models.Movie;
import com.miguelrosa.cinepass.Domain.Models.TVSeries;
import com.miguelrosa.cinepass.Domain.Responses.AccountDetailsResponse;
import com.miguelrosa.cinepass.Domain.Responses.CastResponse;
import com.miguelrosa.cinepass.Domain.Responses.FavoriteResponse;
import com.miguelrosa.cinepass.Domain.Responses.MovieResponse;
import com.miguelrosa.cinepass.Domain.Responses.PopularMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.PopularTVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.RequestTokenResponse;
import com.miguelrosa.cinepass.Domain.Responses.SessionResponse;
import com.miguelrosa.cinepass.Domain.Responses.TVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TopRatedMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.TopRatedTVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.UpComingMoviesResponse;
import com.miguelrosa.cinepass.Domain.Responses.UpComingTVSeriesResponse;
import com.miguelrosa.cinepass.Domain.Responses.VideoResponse;
import com.miguelrosa.cinepass.Domain.Responses.WatchProviderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/upcoming")
    Call<UpComingMoviesResponse> getUpComingMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @POST("account/{account_id}/favorite")
    Call<FavoriteResponse> markAsFavorite(
            @Path("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Body FavoriteRequest body);

    @GET("account/{account_id}/favorite/movies")
    Call<MovieResponse> getFavoriteMovies(
            @Query("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Query("language") String language);

    @GET("authentication/token/new")
    Call<RequestTokenResponse> getRequestToken(@Query("api_key") String apiKey);

    @POST("authentication/token/validate_with_login")
    Call<RequestTokenResponse> validateWithLogin(
            @Query("api_key") String apiKey,
            @Query("username") String username,
            @Query("password") String password,
            @Query("request_token") String requestToken);

    @POST("authentication/session/new")
    Call<SessionResponse> createSession(
            @Body CreateSessionBody createSessionBody,
            @Query("api_key") String apiKey);

    @POST("account/{account_id}/watchlist")
    Call<FavoriteResponse> addToWatchlist(
            @Path("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Body FavoriteRequest request);

    @GET("account/{account_id}/watchlist/movies")
    Call<MovieResponse> getWatchlistMovies(
            @Path("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Query("language") String language);

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/watch/providers")
    Call<WatchProviderResponse> getWatchProviders(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language
    );

    @GET("account")
    Call<AccountDetailsResponse> getAccountDetails(
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @DELETE("authentication/session")
    Call<Void> deleteSession(
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getMovieCredits(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("tv/{series_id}")
    Call<TVSeries> getTVSeriesDetails(
            @Path("series_id") int seriesId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/popular")
    Call<PopularTVSeriesResponse> getPopularTVSeries(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("tv/on_the_air")
    Call<UpComingTVSeriesResponse> getUpComingTVSeries(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("tv/top_rated")
    Call<TopRatedTVSeriesResponse> getTopRatedTVSeries(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("search/tv")
    Call<TVSeriesResponse> searchTVSeries(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language
    );

    @GET("tv/{series_id}/watch/providers")
    Call<WatchProviderResponse> getTVSeriesWatchProviders(
            @Path("series_id") int seriesId,
            @Query("api_key") String apiKey
    );

    @GET("tv/{series_id}/credits")
    Call<CastResponse> getTVSeriesCredits(
            @Path("series_id") int seriesId,
            @Query("api_key") String apiKey
    );

    @GET("tv/{series_id}/videos")
    Call<VideoResponse> getTVSeriesVideos(
            @Path("series_id") int seriesId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("account/{account_id}/favorite/tv")
    Call<TVSeriesResponse> getFavoriteTVSeries(
            @Query("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Query("language") String language);

    @GET("account/{account_id}/watchlist/tv")
    Call<TVSeriesResponse> getWatchlistTVSeries(
            @Path("account_id") int accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Query("language") String language);
}

