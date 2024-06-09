package com.miguelrosa.cinepass.Domain.Responses;

import com.miguelrosa.cinepass.Domain.Models.Video;

import java.util.List;

public class VideoResponse {
    private int id;
    private List<Video> results;

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
