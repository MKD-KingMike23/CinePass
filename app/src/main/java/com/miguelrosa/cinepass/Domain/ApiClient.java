package com.miguelrosa.cinepass.Domain;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "de92655480bdf53d5ffefeeff6e7e99f";

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
}
