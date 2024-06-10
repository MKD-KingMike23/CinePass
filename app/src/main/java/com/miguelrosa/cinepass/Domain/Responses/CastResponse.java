package com.miguelrosa.cinepass.Domain.Responses;

import com.miguelrosa.cinepass.Domain.Models.Cast;

import java.util.List;

public class CastResponse {
    private List<Cast> cast;

    // Getters and setters
    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}
