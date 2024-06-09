package com.miguelrosa.cinepass.Domain.Responses;

import com.google.gson.annotations.SerializedName;
import com.miguelrosa.cinepass.Domain.Models.Genre;

import java.util.List;

public class GenreResponse {
    @SerializedName("genres")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
