package com.miguelrosa.cinepass.Activities;

import static android.view.View.GONE;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.miguelrosa.cinepass.Adapters.CastAdapter;
import com.miguelrosa.cinepass.Adapters.GenreAdapter;
import com.miguelrosa.cinepass.Adapters.TrailerAdapter;
import com.miguelrosa.cinepass.Adapters.WatchProviderAdapter;
import com.miguelrosa.cinepass.Domain.ApiClient;
import com.miguelrosa.cinepass.Domain.Models.Cast;
import com.miguelrosa.cinepass.Domain.Models.FavoriteRequest;
import com.miguelrosa.cinepass.Domain.Responses.CastResponse;
import com.miguelrosa.cinepass.Domain.Responses.FavoriteResponse;
import com.miguelrosa.cinepass.Domain.Models.Genre;
import com.miguelrosa.cinepass.Domain.Models.Movie;
import com.miguelrosa.cinepass.Domain.Responses.MovieResponse;
import com.miguelrosa.cinepass.Domain.Models.Video;
import com.miguelrosa.cinepass.Domain.Responses.VideoResponse;
import com.miguelrosa.cinepass.Domain.Responses.WatchProviderResponse;
import com.miguelrosa.cinepass.R;
import com.miguelrosa.cinepass.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener{
    private ActivityDetailBinding binding;
    private int movieId, accountId;
    private String sessionId;
    private RecyclerView.Adapter genreAdapter, trailerAdapter, watchProviderAdapter, castAdapter;
    private List<Genre> genreList = new ArrayList<>();
    private boolean isFavorite = false, isWatchlisted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("CinePassPrefs", MODE_PRIVATE);
        sessionId = preferences.getString("sessionId", null);
        accountId = preferences.getInt("accountId", -1);

        movieId = getIntent().getIntExtra("id", 0);

        binding.trailersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.watchProvidersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.genreRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        genreAdapter = new GenreAdapter(genreList);
        binding.genreRecycler.setAdapter(genreAdapter);

        binding.rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.imageViewBack.setOnClickListener(v -> finish());
        fetchMovieDetails(movieId);
        fetchMovieTrailers(movieId);
        fetchWatchProviders(movieId);
        fetchMovieCast(movieId);

        binding.imageViewFav.setOnClickListener(v -> toggleFavorite());
        binding.imageViewWatchlist.setOnClickListener(v -> toggleWatchlist());
    }

    @Override
    public void onTrailerClicked(String videoUrl) {
        showTrailer(videoUrl);
    }

    private void fetchMovieDetails(int movieId) {
        binding.progressBarDetail.setVisibility(View.VISIBLE);
        binding.scrollViewDetail.setVisibility(GONE);
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<Movie> call = ApiClient.getTmdbApiService().getMovieDetails(movieId, apiKey, language);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        binding.progressBarDetail.setVisibility(GONE);
                        binding.scrollViewDetail.setVisibility(View.VISIBLE);

                        binding.movieNameTxt.setText(movie.getTitle());
                        binding.movieSummary.setText(movie.getOverview());
                        binding.movieReleaseDate.setText(movie.getReleaseDate());
                        binding.movieRuntime.setText(String.valueOf(movie.getRuntime())+ " min");
                        binding.movieStar.setText(String.valueOf(movie.getVoteAverage()));
                        binding.movieTime.setText(String.valueOf(movie.getVoteCount()));

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

                        Glide.with(DetailActivity.this)
                                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                                .apply(requestOptions)
                                .into(binding.picDetail);

                        List<Genre> genres = movie.getGenres();
                        if (genres != null) {
                            genreAdapter = new GenreAdapter(genres);
                            binding.genreRecycler.setAdapter(genreAdapter);
                        }
                        checkIfFavorite(movieId, sessionId);
                        checkIfWatchlist(movieId, sessionId);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieTrailers(int movieId) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<VideoResponse> call = ApiClient.getTmdbApiService().getMovieVideos(movieId, apiKey, language);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    VideoResponse videoResponse = response.body();
                    if (videoResponse != null) {
                        binding.textView23.setVisibility(View.VISIBLE);

                        List<Video> trailers = new ArrayList<>();
                        for (Video video : videoResponse.getResults()) {
                            if ("Trailer".equalsIgnoreCase(video.getType())) {
                                trailers.add(video);
                            }
                        }
                        if (trailers.isEmpty()){
                            binding.textView23.setVisibility(GONE);
                        }

                        trailerAdapter = new TrailerAdapter(trailers, DetailActivity.this);
                        binding.trailersRecycler.setAdapter(trailerAdapter);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWatchProviders(int movieId) {
        String apiKey = ApiClient.getApiKey();

        Call<WatchProviderResponse> call = ApiClient.getTmdbApiService().getWatchProviders(movieId, apiKey);
        call.enqueue(new Callback<WatchProviderResponse>() {
            @Override
            public void onResponse(Call<WatchProviderResponse> call, Response<WatchProviderResponse> response) {
                if (response.isSuccessful()) {
                    WatchProviderResponse watchProviderResponse = response.body();
                    if (watchProviderResponse != null && watchProviderResponse.getResults().containsKey("ES")) {
                        binding.textView24.setVisibility(View.VISIBLE);
                        WatchProviderResponse.ProviderCountry providerCountry = watchProviderResponse.getResults().get("ES");
                        List<WatchProviderResponse.Provider> providers = providerCountry.getFlatrate();
                        if (providers != null) {
                            WatchProviderAdapter adapter = new WatchProviderAdapter(providers);
                            binding.watchProvidersRecycler.setAdapter(adapter);
                        } else {
                            binding.textView24.setVisibility(GONE);
                        }
                    } else {
                        binding.textView24.setVisibility(GONE);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WatchProviderResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieCast(int movieId) {
        String apiKey = ApiClient.getApiKey();
        Call<CastResponse> call = ApiClient.getTmdbApiService().getMovieCredits(movieId, apiKey);

        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                if (response.isSuccessful()) {
                    CastResponse castResponse = response.body();
                    if (castResponse != null) {
                        List<Cast> castList = castResponse.getCast();
                        castAdapter = new CastAdapter(castList, DetailActivity.this);
                        binding.rvCast.setAdapter(castAdapter);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        String apiKey = ApiClient.getApiKey();
        FavoriteRequest request = new FavoriteRequest("movie", movieId, !isFavorite, !isWatchlisted);

        Call<FavoriteResponse> call = ApiClient.getTmdbApiService().markAsFavorite(accountId, apiKey, sessionId, request);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful()) {
                    isFavorite = !isFavorite;
                    updateFavoriteIcon();
                    if (isFavorite) {
                        Toast.makeText(DetailActivity.this, "Película añadida a Favoritos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Película eliminada de Favoritos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: 1" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: 2" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleWatchlist() {
        String apiKey = ApiClient.getApiKey();
        FavoriteRequest request = new FavoriteRequest("movie", movieId, !isFavorite , !isWatchlisted);

        Call<FavoriteResponse> call = ApiClient.getTmdbApiService().addToWatchlist(accountId, apiKey, sessionId, request);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful()) {
                    isWatchlisted = !isWatchlisted;
                    updateWatchlistIcon();
                    if (isWatchlisted) {
                        Toast.makeText(DetailActivity.this, "Película añadida a Pendientes", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Película eliminada de Pendientes", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error: 1" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: 2" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfFavorite(int movieId, String sessionId) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<MovieResponse> call = ApiClient.getTmdbApiService().getFavoriteMovies(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> favoriteMovies = movieResponse.getResults();
                        for (Movie movie : favoriteMovies) {
                            if (movie.getId() == movieId) {
                                isFavorite = true;
                                updateFavoriteIcon();
                                return;
                            }
                        }
                    }
                    isFavorite = false;
                    updateFavoriteIcon();
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfWatchlist(int movieId, String sessionId) {
        String apiKey = ApiClient.getApiKey();
        String language = "es-ES";

        Call<MovieResponse> call = ApiClient.getTmdbApiService().getWatchlistMovies(accountId, apiKey, sessionId, language);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> watchlistMovies = movieResponse.getResults();
                        for (Movie movie : watchlistMovies) {
                            if (movie.getId() == movieId) {
                                isWatchlisted = true;
                                updateWatchlistIcon();
                                return;
                            }
                        }
                    }
                    isWatchlisted = false;
                    updateWatchlistIcon();
                } else {
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTrailer(String videoUrl) {
        WebSettings webSettings = binding.trailerWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.trailerWebView.setWebViewClient(new WebViewClient());
        binding.trailerWebView.setWebChromeClient(new MyWebChromeClient());

        String html = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        binding.trailerWebView.loadData(html, "text/html", "utf-8");
        binding.trailerWebView.setVisibility(View.VISIBLE);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);

            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                binding.trailerWebView.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                binding.trailerWebView.setVisibility(View.INVISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }


    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.imageViewFav.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            binding.imageViewFav.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }

    private void updateWatchlistIcon() {
        if (isWatchlisted) {
            binding.imageViewWatchlist.setImageResource(R.drawable.baseline_check_24);
        } else {
            binding.imageViewWatchlist.setImageResource(R.drawable.baseline_add_24);
        }
    }
}