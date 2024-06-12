package com.miguelrosa.cinepass.Domain.Responses;

import com.google.gson.annotations.SerializedName;
import com.miguelrosa.cinepass.Domain.Models.TVSeries;

import java.util.List;

public class UpComingTVSeriesResponse {
    @SerializedName("results")
    private List<TVSeries> results;

    // getters and setters

    public List<TVSeries> getResults() {
        return results;
    }

    public void setResults(List<TVSeries> results) {
        this.results = results;
    }
}
