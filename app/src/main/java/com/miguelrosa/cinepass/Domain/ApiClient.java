package com.miguelrosa.cinepass.Domain;

import com.miguelrosa.cinepass.Domain.Responses.RequestTokenResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "de92655480bdf53d5ffefeeff6e7e99f";
    private static final String language = "es-ES";

    private static Retrofit retrofit = null;

    public static TmdbApiService getTmdbApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TmdbApiService.class);
    }

    public static String getApiKey() {
        return API_KEY;
    }
    public static Call<RequestTokenResponse> getRequestToken() {
        return getTmdbApiService().getRequestToken(API_KEY);
    }

    public static String getLanguage() {
        return language;
    }
}
